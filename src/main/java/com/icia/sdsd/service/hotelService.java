package com.icia.sdsd.service;

import com.icia.sdsd.dao.ReviewRepository;
import com.icia.sdsd.dao.STRepository;
import com.icia.sdsd.dao.favRepository;
import com.icia.sdsd.dto.*;
import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;

@Service
@RequiredArgsConstructor
public class hotelService {

    private ModelAndView mav=new ModelAndView();

    private final STRepository strepo;

    private final favRepository favRepo;

    private final ReviewRepository rrepo;

    private final HttpSession session;

    // ----------------------------------- 크롤링 영역 -----------------------------------
    // 호텔목록 크롤링
    public List<crawlingDataDTO> searchCrawling(crawlingDTO data) {
        List<crawlingDataDTO> results=new ArrayList<>();
        crawlingDataDTO result=null;
        String url="";
        // 입력한 정보에 맞는 여기어때 사이트 주소
        if(data.getMinPrice()==10000 && data.getMaxPrice()==300000) {
            url = "https://www.goodchoice.kr/product/search/" + data.getTypeCode() + "/" + data.getAreaCode() + "?sel_date=" + data.getSel_date() + "&sel_date2=" + data.getSel_date2();
        }else{
            url = "https://www.goodchoice.kr/product/search/" + data.getTypeCode() + "/" + data.getAreaCode() + "?sel_date=" + data.getSel_date() + "&sel_date2=" + data.getSel_date2()+"&min_price="+ data.getMinPrice()+"&max_price="+data.getMaxPrice();
        }
        System.out.println(url);

        Document doc=null;
        try {
            doc = Jsoup.connect(url).get();
            // 각각의 호텔 정보가 담긴 li 태그 모두 선택
            Elements elements = doc.select("div#poduct_list_area li");
            // 각각의 호텔에서 정보 추출
            for (Element e : elements) {
                Element info = e.selectFirst("a");

                Element image = e.selectFirst("img");
                Element name = e.selectFirst("div.name strong");
                Element score = e.selectFirst("div.name p.score em");
                Element reviewCount = e.selectFirst("div.name p.score");
                Element position = e.selectFirst("div.name p:nth-of-type(2)");
                Element price = e.selectFirst("div.price div.map_html p:nth-of-type(2) b");
                Element price2 = e.selectFirst("div.price div.map_html p:nth-of-type(1) b");

                result=new crawlingDataDTO();
                result.setAno(info.attr("data-ano").trim());
                result.setAdcno(info.attr("data-adcno").trim());
                // 링크를 우리 페이지에서 동작하도록 수정
                String modifyLink=info.attr("href").trim().substring(33);
                result.setLink(modifyLink);
                result.setAlat(info.attr("data-alat").trim());
                result.setAlng(info.attr("data-alng").trim());
                result.setDistance(info.attr("data-distance").trim());
                result.setImage(image.attr("data-original").trim());
                result.setName(name.text().trim());
                // 별점이 없는 경우 에러 발생 방지
                if(score!=null)
                    result.setScore(score.text().trim());
                result.setReviewCount(String.valueOf(reviewCount));
                result.setPosition((position.text().trim()));
                // 태그에 가격정보가 없는 경우 에러 발생 방지
                if(price!=null)
                    result.setPrice((price.text().trim()));
                else
                    result.setPrice((price2.text().trim()));
                result.setSel_date(data.getSel_date());
                result.setSel_date2(data.getSel_date2());
                boolean check=true;
                for(crawlingDataDTO cra:results)
                    if(result.getAno().equals(cra.getAno())) {
                        check = false;
                        System.out.println("중복 : "+result.getAno());
                    }
                if(check)
                    results.add(result);

            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println(results);
        return results;


    }

    // 선택한 정보로 호텔 상세 페이지 정보 가져오기
    public ModelAndView detail(crawlingDTO data) {
        mav.clear();
        // 최근 본 숙소(localStorage에 날짜 정보 없이 저장) 날짜 처리를 위한 오늘날짜, 내일날짜 구하기
        Calendar cal = Calendar.getInstance();
        // 필요한 포맷으로 설정
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        // 해당 포맷으로 오늘 날짜 설정
        String today = sdf.format(cal.getTime());
        System.out.println(today);
        // 해당 포맷으로 내일 날짜 설정
        cal.add(cal.DATE, +1);
        String tomorrow = sdf.format(cal.getTime());
        System.out.println(tomorrow);
        // 날짜 정보가 없을시 생성한 오늘, 내일 정보 입력
        if(data.getSel_date()==null || data.getSel_date2()==null) {
            data.setSel_date(today);
            data.setSel_date2(tomorrow);
        }

        crawlingViewDTO result=new crawlingViewDTO();
        // 입력한 정보에 맞는 여기어때 사이트 주소
        String url="https://www.goodchoice.kr/product/detail?ano="+data.getAno()+"&adcno="+data.getAdcno()+"&sel_date="+data.getSel_date()+"&sel_date2="+data.getSel_date2();
        Document doc=null;
        // 호텔 정보 크롤링 영역
        try {
            doc=Jsoup.connect(url).get();
            String name=doc.select("div.info h2").text();
            String score=doc.select("div.score_cnt span").text();
            String tel=doc.select("div.score_cnt p a").attr("href");
            String address=doc.select("div.info p.address").text();
            String comment=doc.select("div.comment div").html();
            String defaultInfo=doc.select("section.default_info").html();
            String sellerInfo=doc.select("section.seller_info").html();
            result.setName(name);
            result.setScore(score);
            result.setTel(tel);
            result.setAddress(address);
            result.setComment(comment);
            result.setDefaultInfo(defaultInfo);
            result.setSellerInfo(sellerInfo);
            result.setAno(data.getAno());
            result.setAdcno(data.getAdcno());
            result.setSel_date(data.getSel_date());
            result.setSel_date2(data.getSel_date2());
            // 탭이동을 위한 탭정보가 있는 경우
            if(data.getTabs()!=null)
                result.setTabs((data.getTabs()));

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        // 호텔 이미지 크롤링 영역
        List<String> images=new ArrayList<>();
        String image=null;
        try {
            doc= Jsoup.connect(url).get();
            Elements element=doc.select("div.swiper-container.gallery-top");
            Iterator<Element> imageList=element.select("img").iterator();
            while(imageList.hasNext()){
                image=imageList.next().attr("data-src").trim();
                images.add(image);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        // 호텔 방정보 크롤링 영역
        List<crawlingViewRoomDTO> rooms=new ArrayList<>();
        crawlingViewRoomDTO room=null;
        try {
            doc= Jsoup.connect(url).get();
            Elements elements=doc.select("div.room");
            for(Element e : elements){
                Element roomImage=e.selectFirst("p.pic_view img");
                Element roomInfo=e.selectFirst("strong.title");
                Element roomPrice=e.selectFirst("div.info div.half:nth-of-type(2) b");
                Element roomPrice2=e.selectFirst("div.info div.fast b");

                room=new crawlingViewRoomDTO();
                room.setRoomImage(roomImage.attr("data-original").trim());
                room.setRoomInfo(roomInfo.text().trim());
                // 대실 가격만 있거나, 가격 정보가 없는 경우 처리를 위한 영역
                if(roomPrice!=null)
                    room.setRoomPrice(roomPrice.text().trim());
                else {
                    if(roomPrice2==null)
                        room.setRoomPrice("숙소에 문의");
                    else
                        room.setRoomPrice(roomPrice2.text().trim());
                }
                rooms.add(room);
            }
//            Iterator<Element> roomImage=element.select("p.pic_view img").iterator();
//            Iterator<Element> roomInfo=element.select("strong.title").iterator();
//            Iterator<Element> roomPrice=element.select("div.info div.half:nth-of-type(2) b").iterator();
//            Elements check=element.select("div.info div.half:nth-of-type(2) b");
//            Elements checkHalf=element.select("div.info div.half.none");
//            System.out.println("checkHalf : "+checkHalf);
//            if(check.isEmpty()){
//                roomPrice=element.select("div.info div.fast b").iterator();
//                System.out.println("2박 이상"+roomPrice);
//            }
//            while(roomImage.hasNext()){
//                room=new crawlingViewRoomDTO();
//                room.setRoomImage(roomImage.next().attr("data-original").trim());
//                room.setRoomInfo(roomInfo.next().text().trim());
//                if(!checkHalf.isEmpty()) {
//                    room.setRoomPrice("숙소에 문의");
//                    checkHalf=checkHalf.next().next();
//                    System.out.println("checkHalf : "+checkHalf);
//                }else{
//                    room.setRoomPrice(roomPrice.next().text().trim());
//                }
//                rooms.add(room);
//            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println(rooms);
        System.out.println(images);

        mav.addObject("images",images);
        mav.addObject("rooms", rooms);
        mav.addObject("result", result);
        mav.setViewName("/hotel/view");
        return mav;
    }

    // 선택한 정보를 예약 페이지로 가져가기
    public ModelAndView reserve(crawlingDTO data) {
        mav.clear();
        List<String> images=(List<String>)detail(data).getModel().get("images");
        String image= images.get(0);
        String url="/detail?ano="+data.getAno()+"&adcno="+data.getAdcno();;


        System.out.println(image);
        crawlingDataDTO result=new crawlingDataDTO();
        result.setPrice(data.getTotalPrice());
        result.setLink(url);
        result.setImage(image);
        result.setName(data.getHotelName());
        result.setSel_date(data.getSel_date());
        result.setSel_date2(data.getSel_date2());
        result.setPosition(data.getRoomInfo());
        result.setAno(data.getAno());
        result.setAdcno(data.getAdcno());

        mav.addObject("result",result);
        mav.setViewName("/hotel/payment");
        System.out.println(mav);
        return mav;
    }

    // 검색어로 호텔목록 크롤링
    public List<crawlingDataDTO> searchKeyword(String keyword) {
        List<crawlingDataDTO> results=new ArrayList<>();
        crawlingDataDTO result=null;
        String url="https://www.goodchoice.kr/product/result?keyword=모텔+"+keyword;
        System.out.println(url);

        Document doc=null;
        try {
            doc = Jsoup.connect(url).get();
            Elements elements = doc.select("div#poduct_list_area li");
            for (Element e : elements) {
                Element info = e.selectFirst("a");

                Element image = e.selectFirst("img");
                Element name = e.selectFirst("div.name strong");
                Element score = e.selectFirst("div.name p.score em");
                Element reviewCount = e.selectFirst("div.name p.score");
                Element position = e.selectFirst("div.name p:nth-of-type(2)");
                Element price = e.selectFirst("div.price div.map_html p:nth-of-type(2) b");
                Element price2 = e.selectFirst("div.price div.map_html p:nth-of-type(1) b");

                result=new crawlingDataDTO();
                result.setAno(info.attr("data-ano").trim());
                result.setAdcno(info.attr("data-adcno").trim());
                String modifyLink=info.attr("href").trim().substring(33);
                result.setLink(modifyLink);
                result.setAlat(info.attr("data-alat").trim());
                result.setAlng(info.attr("data-alng").trim());
                result.setDistance(info.attr("data-distance").trim());
                result.setImage(image.attr("data-original").trim());
                result.setName(name.text().trim());
                if(score!=null)
                    result.setScore(score.text().trim());
                result.setReviewCount(String.valueOf(reviewCount));
                result.setPosition((position.text().trim()));
                if(price!=null)
                    result.setPrice((price.text().trim()));
                else
                    result.setPrice((price2.text().trim()));
                results.add(result);

            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println(results);
        return results;


    }
//        try {
//            doc= Jsoup.connect(url).get();
//            Elements element=doc.select("div#poduct_list_area");
//
//            Iterator<Element> ano=element.select("a").iterator();
//            Iterator<Element> adcno=element.select("a").iterator();
//            Iterator<Element> link=element.select("a").iterator();
//            Iterator<Element> alat=element.select("a").iterator();
//            Iterator<Element> alng=element.select("a").iterator();
//            Iterator<Element> distance=element.select("a").iterator();
//            Iterator<Element> image=element.select("a p img").iterator();
//            Iterator<Element> name=element.select("a div div.name strong").iterator();
//            Iterator<Element> score=element.select("a div div.name p.score").iterator();
//            Iterator<Element> reviewCount=element.select("a div div.name p.score").iterator();
//            Iterator<Element> position=element.select("a div div.name p:nth-of-type(2)").iterator();
//            Iterator<Element> price=element.select("a div div.price div.map_html p b").iterator();
//            while(ano.hasNext()){
//                result=new crawlingDataDTO();
//                result.setAno(ano.next().attr("data-ano").trim());
//                result.setAdcno(adcno.next().attr("data-adcno").trim());
//                String modifyLink=link.next().attr("href").trim().substring(33);
//                result.setLink(modifyLink);
//                result.setAlat(alat.next().attr("data-alat").trim());
//                result.setAlng(alng.next().attr("data-alng").trim());
//                result.setDistance(distance.next().attr("data-distance").trim());
//                result.setImage(image.next().attr("data-original").trim());
//                result.setName(name.next().text().trim());
//                result.setScore(score.next().select("em").text().trim());
//                result.setReviewCount(String.valueOf(reviewCount.next()));
//                result.setPosition((position.next().text().trim()));
//                result.setPrice((price.next().text().trim()));
//
//                results.add(result);
//
//            }
//
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//        System.out.println(results);
//        return results;
//    }

    // 검색어, 날짜, 가격정보로 호텔목록 크롤링
    public List<crawlingDataDTO> searchCondition(crawlingDTO data) {
        List<crawlingDataDTO> results=new ArrayList<>();
        crawlingDataDTO result=null;
        String url="https://www.goodchoice.kr/product/result?keyword=모텔+"+data.getKeyword()+"&sel_date="+data.getSel_date()+"&sel_date2="+data.getSel_date2()+"&min_price="+ data.getMinPrice()+"&max_price="+data.getMaxPrice();
        System.out.println(url);

        Document doc=null;
        try {
            doc = Jsoup.connect(url).get();
            Elements elements = doc.select("div#poduct_list_area li");
            for (Element e : elements) {
                Element info = e.selectFirst("a");

                Element image = e.selectFirst("img");
                Element name = e.selectFirst("div.name strong");
                Element score = e.selectFirst("div.name p.score em");
                Element reviewCount = e.selectFirst("div.name p.score");
                Element position = e.selectFirst("div.name p:nth-of-type(2)");
                Element price = e.selectFirst("div.price div.map_html p:nth-of-type(2) b");
                Element price2 = e.selectFirst("div.price div.map_html p:nth-of-type(1) b");

                result=new crawlingDataDTO();
                result.setAno(info.attr("data-ano").trim());
                result.setAdcno(info.attr("data-adcno").trim());
                String modifyLink=info.attr("href").trim().substring(33);
                result.setLink(modifyLink);
                result.setAlat(info.attr("data-alat").trim());
                result.setAlng(info.attr("data-alng").trim());
                result.setDistance(info.attr("data-distance").trim());
                result.setImage(image.attr("data-original").trim());
                result.setName(name.text().trim());
                if(score!=null)
                    result.setScore(score.text().trim());
                result.setReviewCount(String.valueOf(reviewCount));
                result.setPosition((position.text().trim()));
                if(price!=null)
                    result.setPrice((price.text().trim()));
                else
                    result.setPrice((price2.text().trim()));
                result.setSel_date(data.getSel_date());
                result.setSel_date2(data.getSel_date2());
                results.add(result);

            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
//        try {
//            doc= Jsoup.connect(url).get();
//            Elements element=doc.select("div#poduct_list_area");
//
//            Iterator<Element> ano=element.select("a").iterator();
//            Iterator<Element> adcno=element.select("a").iterator();
//            Iterator<Element> link=element.select("a").iterator();
//            Iterator<Element> alat=element.select("a").iterator();
//            Iterator<Element> alng=element.select("a").iterator();
//            Iterator<Element> distance=element.select("a").iterator();
//            Iterator<Element> image=element.select("a p img").iterator();
//            Iterator<Element> name=element.select("a div div.name strong").iterator();
//            Iterator<Element> score=element.select("a div div.name p.score").iterator();
//            Iterator<Element> reviewCount=element.select("a div div.name p.score").iterator();
//            Iterator<Element> position=element.select("a div div.name p:nth-of-type(2)").iterator();
//            Iterator<Element> price=element.select("a div div.price div.map_html p b").iterator();
//            while(ano.hasNext()){
//                result=new crawlingDataDTO();
//                result.setAno(ano.next().attr("data-ano").trim());
//                result.setAdcno(adcno.next().attr("data-adcno").trim());
//                String modifyLink=link.next().attr("href").trim().substring(33);
//                result.setLink(modifyLink);
//                result.setAlat(alat.next().attr("data-alat").trim());
//                result.setAlng(alng.next().attr("data-alng").trim());
//                result.setDistance(distance.next().attr("data-distance").trim());
//                result.setImage(image.next().attr("data-original").trim());
//                result.setName(name.next().text().trim());
//                result.setScore(score.next().select("em").text().trim());
//                result.setReviewCount(String.valueOf(reviewCount.next()));
//                result.setPosition((position.next().text().trim()));
//                result.setPrice((price.next().text().trim()));
//                result.setSel_date(data.getSel_date());
//                result.setSel_date2(data.getSel_date2());
//
//                results.add(result);
//
//            }
//
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
        System.out.println(results);
        return results;
    }

    // 호텔 추천목록 크롤링, 사용 안 함
    public List<crawlingDataDTO> recommandCrawling() {
        List<crawlingDataDTO> results=new ArrayList<>();
        crawlingDataDTO result=null;
        String url="https://www.goodchoice.kr/product/home/3";
        Document doc=null;
        try {
            doc = Jsoup.connect(url).get();
            Elements elements = doc.select("div#poduct_list_area li");
            for (Element e : elements) {
                Element info = e.selectFirst("a");
                Element image = e.selectFirst("img");
                Element name = e.selectFirst("div.name strong");
                Element score = e.selectFirst("div.name p.score em");
                Element reviewCount = e.selectFirst("div.name p.score");
                Element position = e.selectFirst("div.name p:nth-of-type(2)");
                Element price = e.selectFirst("div.price div.map_html p:nth-of-type(2) b");
                Element price2 = e.selectFirst("div.price div.map_html p:nth-of-type(1) b");

                result=new crawlingDataDTO();
                result.setAno(info.attr("data-ano").trim());
                result.setAdcno(info.attr("data-adcno").trim());
                String modifyLink=info.attr("href").trim().substring(33);
                result.setLink(modifyLink);
                result.setAlat(info.attr("data-alat").trim());
                result.setAlng(info.attr("data-alng").trim());
                result.setDistance(info.attr("data-distance").trim());
                result.setImage(image.attr("data-original").trim());
                result.setName(name.text().trim());
                if(score!=null)
                    result.setScore(score.text().trim());
                result.setReviewCount(String.valueOf(reviewCount));
                result.setPosition((position.text().trim()));
                if(price!=null)
                    result.setPrice((price.text().trim()));
                else
                    result.setPrice((price2.text().trim()));
                results.add(result);

            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println(results);
        return results;

    }


    // ----------------------------------- 예약 및 결제 영역 -----------------------------------

    // 결제 성공시 결제완료 (stNum == 1)로 표시되게 하는 함수
    public String successPay(String stNum) {
        /* System.out.println("ticket: "+ ticket);*/

        String result = "NO";
        TicketingEntity entity = strepo.findById(stNum).get();
        entity.setStPay(1);
        System.out.println("entity : " + entity);
        try{
            strepo.save(entity);
            result = "OK";
        } catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }

    // 결제 실행전 db 입력
    public String insertDB(TicketingDTO ticket) {
        System.out.println("ticket: "+ ticket);
        TicketingEntity entity = TicketingEntity.toEntity(ticket);

        // 결제 전
        entity.setStPay(0);
        String result = null;

        // STNUM(예약번호 생성)
        String uuid = UUID.randomUUID().toString().substring(0,6);
        entity.setStNum("RN-"+uuid);
        System.out.println("entity : "+entity);
        try{
            strepo.save(entity);
            result = entity.getStNum();
        } catch (Exception e){
            e.printStackTrace();
        }

        return result;
    }

    // 미결제 내역 재결재
    public ModelAndView rePay(String stNum) {
        mav = new ModelAndView();
        // entity에 기존 정보 담기
        TicketingEntity entity = strepo.findById(stNum).get();

        System.out.println("재결재 : "+entity);

        mav.addObject("result",entity);
        mav.setViewName("hotel/reservPayment");
        return mav;
    }

    // 미결제 내역 삭제(기존 결제내역 삭제)
    public String ticketingDel(String stNum) {


        // 기존 정보 삭제
        strepo.deleteById(stNum);

        // 세션에서 Id 값 받아오기 (object를 string값으로 변환)
        String Id = (String)session.getAttribute("loginId");

        // 다시 내정보 페이지로 이동
        return "redirect:/mView/"+Id+"/menu2";
    }

    // 예약내역 불러오기
    public List<TicketingDTO> tkList(String tkId) {
        System.out.println(tkId);
        List<TicketingDTO>TicketList = new ArrayList<>();
        List<TicketingEntity>TicketEntity = strepo.findByStId(tkId);
        System.out.println(TicketEntity);
        for (TicketingEntity entity : TicketEntity){
            TicketList.add(TicketingDTO.toDTO(entity));
        }
        System.out.println("TicketList : "+ TicketList);

        return TicketList;
    }

    // 예약취소(결제가 된것은 환불 요청으로 미결제 건은 바로 지우기)
    public ModelAndView deletePay(String stNum) {
        mav= new ModelAndView();

        TicketingEntity entity = strepo.findById(stNum).get();

        int pay = entity.getStPay();
        if(pay == 0){
            strepo.deleteById(stNum);
            mav.setViewName("redirect:/mView/"+entity.getStId()+"/menu3");
        }else{
            entity.setStPay(2);
            strepo.save(entity);
            mav.setViewName("redirect:/mView/"+entity.getStId()+"/menu5");
        }


        return mav;
    }

    public List<TicketingDTO> reservlist() {
        List<TicketingDTO> rList = new ArrayList<>();
        int a=0;
        int b=1;
        List<TicketingEntity> rentityList = strepo.findByStPayOrStPayOrderByStDateDesc(a,b);

        for(TicketingEntity entity : rentityList){
            rList.add(TicketingDTO.toDTO(entity));
        }
        return rList;
    }

    public List<TicketingDTO> reservSearch(SearchDTO search, String board) {
        System.out.println("키워드 : " + search);
        System.out.println(board);

        List<TicketingDTO> reservList = new ArrayList<>();
        List<TicketingEntity> entityList= new ArrayList<>();
        int a=0;
        int b=1;
        if(board.equals("reserv")){
            if (search.getCategory().equals("STID")) {
                entityList = strepo.findByStIdContainingIgnoreCaseAndStPayBetweenOrderByStDateDesc(search.getKeyword(),a,b);
            } else if (search.getCategory().equals("STNUM")) {
                entityList = strepo.findByStNumContainingIgnoreCaseAndStPayBetweenOrderByStDateDesc(search.getKeyword(),a,b);
            }
        }else {
            a=2;
            b=3;
            if (search.getCategory().equals("STID")) {
                entityList = strepo.findByStIdContainingIgnoreCaseAndStPayBetweenOrderByStDateDesc(search.getKeyword(),a,b);
            } else if (search.getCategory().equals("STNUM")) {
                entityList = strepo.findByStNumContainingIgnoreCaseAndStPayBetweenOrderByStDateDesc(search.getKeyword(),a,b);
            } else if (search.getCategory().equals("STPAY")) {
                entityList = strepo.findByStPayOrderByStDateDesc(Integer.parseInt(search.getKeyword()));
            }
        }
        for(TicketingEntity entity : entityList){
            reservList.add(TicketingDTO.toDTO(entity));
        }
        System.out.println(reservList);
        return reservList;
    }

    public List<TicketingDTO> refundList() {
        List<TicketingDTO> rList = new ArrayList<>();
        int a=2;
        int b=3;
        List<TicketingEntity> rentityList = strepo.findByStPayOrStPayOrderByStDateDesc(a,b);

        for(TicketingEntity entity : rentityList){
            rList.add(TicketingDTO.toDTO(entity));
        }
        return rList;
    }

    // 환불요청 승인
    public ModelAndView requestPay(String stNum) {
        mav= new ModelAndView();
        TicketingEntity entity = strepo.findById(stNum).get();
        System.out.println(entity);

        entity.setStPay(3);
        strepo.save(entity);
        mav.setViewName("/admin/ARList");
        return mav;
    }

    // 날짜 지난 결제대기내역 삭제
    public String delLastList() {

        // 모든 내역 찾기
        List<TicketingEntity> entityList = strepo.findAll();
        // 오늘 날짜 구하기
        LocalDate today = LocalDate.now();

        for(TicketingEntity entity : entityList){
            // 날짜 형식을 LocalDate 형식으로 바꿈
            LocalDate date = LocalDate.parse(entity.getStStartDay());
            // 결제대기 내역에 있고 오늘날짜가 결제 대기의 시작 날짜보다 크다면 삭제
            if(entity.getStPay()==0 && today.isAfter(date)){
                System.out.println("삭제할 내역 : " + entity);
                strepo.deleteById(entity.getStNum());
            }
        }
        return "결제대기 내역 삭제 완료";
    }


    // ----------------------------------- 찜 영역 -----------------------------------

    // 찜 목록 추가
    public int favInsert(favDTO fav) {

        System.out.println(fav);
        favEntity entity=favEntity.toEntity(fav);
        favRepo.save(entity);
        int result=entity.getFaNum();
        return result;
    }

    // 찜 목록 삭제
    public void favDel(int faNum) {
        System.out.println(faNum);
        favRepo.delete(favRepo.findById(faNum).get());
    }

    // 찜 여부 확인
    public int favSearch(favDTO fav) {
        int result=0;
        System.out.println(fav);
        Optional<favEntity> check=favRepo.findByFaCodeAndFaLoginId(fav.getFaCode(), fav.getFaLoginId());
        if(check.isPresent())
            result=check.get().getFaNum();

        return result;
    }

    // 찜 목록 가져오기
    public List<favDTO> favSelect(String faLoginId) {
        List<favEntity> entityList=favRepo.findByFaLoginIdOrderByFaNumDesc(faLoginId);
        System.out.println(entityList);
        List<favDTO> result=new ArrayList<>();
        for(favEntity entity : entityList){
            result.add(favDTO.toDTO(entity));
        }
        return result;
    }


    // ----------------------------------- 리뷰 영역 -----------------------------------

    // 리뷰 가져오기
    public List<ReviewDTO> reviewList(String srCode) {

        List<ReviewDTO> result = new ArrayList<>();
        List<ReviewEntity> entityList = rrepo.findBySrCodeOrderBySrNum(srCode);

        for(ReviewEntity entity : entityList){
            result.add(ReviewDTO.toDTO(entity));
        }

        return result;
    }

    public ModelAndView reviewWrite(ReviewDTO review) {
        mav = new ModelAndView();
        ReviewEntity Rentity = rrepo.findById(review.getSrCode()+"_00").get();
       review.setSrDate("1일 전");
       review.setSrNum(review.getSrCode()+"_0-6");
       review.setSrCount(Rentity.getSrCount());
        if(review.getSrGrade()==2){
            review.setSrReviewTitle("조금만 더 신경 써 주세요.");
        }else if(review.getSrGrade()==4){
            review.setSrReviewTitle("조금 아쉬웠지만 이용할만해요.");
        }else if(review.getSrGrade()==6){
            review.setSrReviewTitle("기대 이상이네요.");
        }else if(review.getSrGrade()==8){
            review.setSrReviewTitle("여기라면 다음에 또 이용할 거예요.");
        }else if(review.getSrGrade()==10){
            review.setSrReviewTitle("여기만한 곳은 어디에도 없을 거예요.");
        }
        ReviewEntity rEntity = ReviewEntity.toEntity(review);
        System.out.println(rEntity);
        rrepo.save(rEntity);
        mav.setViewName("redirect:/mView/"+review.getSrId()+"/menu4");
        return mav;
    }
}
