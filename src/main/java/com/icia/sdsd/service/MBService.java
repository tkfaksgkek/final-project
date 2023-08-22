package com.icia.sdsd.service;

import com.icia.sdsd.dao.MBoardRepository;
import com.icia.sdsd.dao.MarketingRequestRepository;
import com.icia.sdsd.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import java.util.*;

@Service
@RequiredArgsConstructor
public class MBService {

    private final MBoardRepository mbrepo;
    private final MarketingRequestRepository mrrepo;

    private final hotelService hsvc;

    private ModelAndView mav=new ModelAndView();

    // 홍보게시판 목록 불러오기
    public List<MBoardDTO> mBoardList() {
        List<MBoardDTO> mbList = new ArrayList<>();
        List<MboardEntity> gbEntityList = mbrepo.findAllByOrderByMbNumDesc();
        System.out.println("mbList : "+gbEntityList);
        for (MboardEntity entity : gbEntityList) {
            mbList.add(MBoardDTO.toDTO(entity));
        }
        return mbList;

    }

    // 홍보게시판 글쓰기
    public ModelAndView MBWrite(MarketingRequestDTO mRequest) {
        mav = new ModelAndView();
        System.out.println(mRequest);
        MboardEntity mbEntity = new MboardEntity();

        // 홍보게시판 내역에 맞게 정보 넣기
        mbEntity.setMbTitle(mRequest.getMrTitle());
        mbEntity.setMbContent(mRequest.getMrContent());
        mbEntity.setMbWriter(mRequest.getMrSoName());
        mbEntity.setMbrNum(mRequest.getMrNum());

        crawlingDTO data = new crawlingDTO();
        data.setAno(mRequest.getMrAno());
        data.setAdcno("1");
        mav = hsvc.detail(data);
        List<String> imgs = (List<String>)mav.getModel().get("images");
        crawlingViewDTO hotel = (crawlingViewDTO)mav.getModel().get("result");

        mbEntity.setMbHotelName(hotel.getName());
        mbEntity.setMbHotelAddr(hotel.getAddress());
        mbEntity.setMbImg1(imgs.get(0));
        mbEntity.setMbImg2(imgs.get(1));
        mbEntity.setMbImg3(imgs.get(2));
        mbEntity.setMbUrl("/detail?ano="+mRequest.getMrAno()+"&adcno=1");
        mbrepo.save(mbEntity);
        // 등록여부 1로 만들기
        MarketingRequestEntity mrEntity = mrrepo.findById(mRequest.getMrNum()).get();
        mrEntity.setMrResponse(1);
        mrrepo.save(mrEntity);

        mav.setViewName("redirect:/MBList");
        return mav;
    }

    // 홍보요청 게시판 상세보기
    public ModelAndView mrView(int mrNum) {
        mav = new ModelAndView();

        MarketingRequestEntity entity = mrrepo.findById(mrNum).get();
        MarketingRequestDTO mRequest = MarketingRequestDTO.toDTO(entity);

        mav.setViewName("/maketingBoard/MRView");
        mav.addObject("view", mRequest);

        return mav;
    }

    // 홍보 요청 게시판 목록
    public List<MarketingRequestDTO> mRequestList() {
        List<MarketingRequestDTO> mrList = new ArrayList<>();
        List<MarketingRequestEntity> entityList = mrrepo.findAllByOrderByMrNumDesc();

        for (MarketingRequestEntity entity : entityList) {
            mrList.add(MarketingRequestDTO.toDTO(entity));
        }
        return mrList;
    }

    // 홍보요청 게시판 글 등록하기
    public ModelAndView mrWrite(MarketingRequestDTO mRequest) {
        mav.clear();
        MarketingRequestEntity entity = MarketingRequestEntity.toEntity(mRequest);
        entity.setMrResponse(0);
        mrrepo.save(entity);
        mav.setViewName("/maketingBoard/MRequest");
        return mav;
    }

    public List<MarketingRequestDTO> mRequestSearch(int mrResponse) {
        List<MarketingRequestDTO> mrList = new ArrayList<>();
        List<MarketingRequestEntity> entityList = mrrepo.findByMrResponseOrderByMrNumDesc(mrResponse);

        for (MarketingRequestEntity entity : entityList) {
            mrList.add(MarketingRequestDTO.toDTO(entity));
        }
        return mrList;
    }

    public String delMR(int mrNum) {
        System.out.println(mrNum);
        mrrepo.deleteById(mrNum);
        Optional<MboardEntity> entity=mbrepo.findByMbrNum(mrNum);
        if(entity.isPresent()) {
            mbrepo.deleteById(entity.get().getMbNum());
        }
        return "OK";
    }

    public int requestSearch(MarketingRequestDTO req) {
        int result=0;
        System.out.println(req);
        if(!req.getMrSoName().isEmpty()) {
            Optional<MarketingRequestEntity> check = mrrepo.findByMrAno(req.getMrAno());
            System.out.println(check);
            if (check.isPresent())
                result = check.get().getMrNum();
        }

        return result;
    }
}
