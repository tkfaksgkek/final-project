package com.icia.sdsd.controller;

import com.google.gson.JsonObject;
import com.icia.sdsd.dto.*;
import com.icia.sdsd.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/* ajax 컨트롤러 */
@RestController
@RequiredArgsConstructor
public class RestfulController {

    private final hotelService hsvc;

    private final MemberService msvc;

    private final OPService opsv;

    private final CMService cmsv;

    private final GBService gbsvc;

    private final MBService mbsvc;

    private final AdminService adsvc;


    // ----------------------------------- 호텔 영역 -----------------------------------

    // 호텔목록 크롤링
    @PostMapping("/searchCrawling")
    public List<crawlingDataDTO> crawlingDTO(crawlingDTO data) {
        return hsvc.searchCrawling(data);
    }

    // 검색어로 호텔목록 크롤링
    @PostMapping("/searchKeyword")
    public List<crawlingDataDTO> searchKeyword(String keyword) {
        return hsvc.searchKeyword(keyword);
    }

    // 검색어, 날짜, 가격정보로 호텔목록 크롤링
    @PostMapping("/searchCondition")
    public List<crawlingDataDTO> searchCondition(crawlingDTO data) {
        return hsvc.searchCondition(data);
    }

    // 호텔 추천목록 크롤링, 사용 안 함
    @PostMapping("recommandCrawling")
    public List<crawlingDataDTO> recommandCrawling() {
        return hsvc.recommandCrawling();
    }

    // 찜 목록 추가
    @PostMapping("/favInsert")
    public int favInsert(favDTO fav) {
        return hsvc.favInsert(fav);
    }

    // 찜 목록 삭제
    @PostMapping("/favDel")
    public void favDel(int faNum) {
        hsvc.favDel(faNum);
    }

    // 찜 여부 확인
    @PostMapping("/favSearch")
    public int favSearch(favDTO fav) {
        return hsvc.favSearch(fav);
    }

    // 찜 목록 가져오기
    @PostMapping("/favSelect")
    public List<favDTO> favSelect(String faLoginId) {
        return hsvc.favSelect(faLoginId);
    }

    // 리뷰 리스트 가져오기
    @PostMapping("/reviewList")
    public List<ReviewDTO> reviewList (@RequestParam("srCode") String srCode){
        return hsvc.reviewList(srCode);
    }


    // ----------------------------------- 회원 영역 -----------------------------------

    @PostMapping("/idCheck")
    public String idCheck(@RequestParam("smId") String smId) {
        return msvc.idCheck(smId);
    }

    @PostMapping("/testLogin")
    public String testLogin(MemberDTO member) {
        return msvc.testLogin(member);
    }

    @PostMapping("/emailCheck")
    public String emailCheck(@RequestParam("smEmail") String smEmail) {
        return msvc.emailCheck(smEmail);
    }

    @PostMapping("/NicknameCheck")
    public String NicknameCheck(@RequestParam("smNickname") String smNickname) {
        return msvc.NicknameCheck(smNickname);
    }

    @PostMapping("/createNick")
    public String createNick() throws Exception {
        return msvc.createNick();
    }

    @PostMapping("/bnCheck")
    public String bnCheck(@RequestParam("soBusnum") String soBusnum) {
        System.out.println(soBusnum);
        return opsv.bnCheck(soBusnum);
    }

    @PostMapping("/emailCheck2")
    public String emailCheck2(@RequestParam("soEmail") String soEmail) {
        return opsv.emailCheck2(soEmail);
    }

    @PostMapping("/phoneCheck")
    public String sendSMS(@RequestParam("smPhone") String smPhone) { // 휴대폰 문자보내기
        int randomNumber = (int) ((Math.random() * (9999 - 1000 + 1)) + 1000);// 난수 생성
        cmsv.phoneCheck(smPhone, randomNumber);
        return Integer.toString(randomNumber);
    }

    @PostMapping("/phoneCheck2")
    public String sendSMS2(@RequestParam("soPhone") String soPhone) { // 휴대폰 문자보내기
        int randomNumber = (int) ((Math.random() * (9999 - 1000 + 1)) + 1000);// 난수 생성
        cmsv.phoneCheck2(soPhone, randomNumber);
        return Integer.toString(randomNumber);
    }

    @PostMapping("/mList")
    public List<MemberDTO> mList() {
        return msvc.mList();
    }

    @PostMapping("/soList")
    public List<OperatorDTO> soList() {
        return opsv.soList();
    }

    @PostMapping("/mSearch")
    public List<MemberDTO> mSearch(@ModelAttribute SearchDTO search) {
        return msvc.mSearch(search);
    }

    @PostMapping("/soSearch")
    public List<OperatorDTO> soSearch(@ModelAttribute SearchDTO search) {
        return opsv.soSearch(search);
    }

    @PostMapping("/findPhone")
    public String findPhone(@ModelAttribute MemberDTO member) {
        return msvc.findPhone(member);
    }

    @PostMapping("/findbyIdPhone")
    public String findbyIdPhone(@ModelAttribute MemberDTO member) {
        return msvc.findbyIdPhone(member);
    }

    @PostMapping("/findPhone2")
    public String findPhone2(@ModelAttribute OperatorDTO operator) {
        return opsv.findPhone2(operator);
    }

    @PostMapping("/findByBnPhone")
    public String findByBnPhone(@ModelAttribute OperatorDTO operator) {
        return opsv.findByBnPhone(operator);
    }


    // ----------------------------------- 자유게시판 영역 -----------------------------------

    @PostMapping("/gBoardList")
    public List<GBoardDTO> gBoardList() {
        return gbsvc.gBoardList();
    }

    @PostMapping("/GBSearch")
    public List<GBoardDTO> GBSearch(@ModelAttribute SearchDTO search) {
        return gbsvc.GBSearch(search);
    }

    // cList : 댓글 목록
    @PostMapping("/cList")
    public List<CommentDTO> cList(@RequestParam("gcBNum") int gcBNum) {
        List<CommentDTO> list = gbsvc.cList(gcBNum);
        return list;
    }
    // countCmt : 댓글 목록
    @PostMapping("/countCmt")
    public String countCmt(@RequestParam int gbNum) {

        return gbsvc.countCmt(gbNum);
    }

    // cWrite : 댓글 작성
    @PostMapping("/cWrite")
    public List<CommentDTO> cWrite(@ModelAttribute CommentDTO cmt){

        List<CommentDTO> list = gbsvc.cWrite(cmt);

        // System.out.println("댓글 갯수 : " + list.size());
        System.out.println(list);
        return list;
    }

    // cModify : 댓글 수정
    @PostMapping("/cModify")
    public List<CommentDTO> cModify(@ModelAttribute CommentDTO cmt) {
        System.out.println("확인 : " + cmt);
        List<CommentDTO> list = gbsvc.cModify(cmt);
        //System.out.println("확인 : " + list);
        return list;
    }

    // 댓글삭제
    @PostMapping("/cDelete")
    public List<CommentDTO> cDelete(@ModelAttribute CommentDTO cmt) {
        return gbsvc.cDelete(cmt);
    }

    // 게시판 이미지 업로드
    @PostMapping(value = "/uploadSummernoteImageFile", produces = "application/json")
    @ResponseBody
    public JsonObject uploadSummernoteImageFile(@RequestParam("file") MultipartFile multipartFile) {
        return gbsvc.uploadSummernoteImageFile(multipartFile);
    }

    // 게시판 이미지 삭제
    @PostMapping("/delImage")
    public String delImage(@RequestParam String delFile) {
        return gbsvc.delImage(delFile);
    }

    // 내가 쓴 글 보기
    @PostMapping("/myGbList")
    public List<GBoardDTO> myGbList(@RequestParam String gbWriterId){
        System.out.println("내가 쓴 글 : " + gbWriterId);
        return gbsvc.myGbList(gbWriterId);
    }

    // index 화면 자유게시판 인기글 불러오기
    @PostMapping("/indexGBlist")
    public List<GBoardDTO> indexGBlist() {
        return gbsvc.indexGBlist();
    }


    // ----------------------------------- 홍보 게시판 영역 -----------------------------------

    @PostMapping("/mBoardList")
    public List<MBoardDTO> mBoardList() {
        return mbsvc.mBoardList();
    }

    // 홍보 요청 게시판
    @PostMapping("/mRequestList")
    public List<MarketingRequestDTO> mRequestList(){ return mbsvc.mRequestList();}

    // 홍보 요청 게시판
    @PostMapping("/mRequestSearch")
    public List<MarketingRequestDTO> mRequestSearch(@RequestParam int mrResponse){ return mbsvc.mRequestSearch(mrResponse);}

    /* 홍보 게시판 글삭제*/
    @PostMapping("/delMR")
    public String delMR(@RequestParam int mrNum) {
        System.out.println(mrNum);
        return mbsvc.delMR(mrNum);
    }

    // 홍보요청 여부 확인
    @PostMapping("/requestSearch")
    public int requestSearch(MarketingRequestDTO req) {
        return mbsvc.requestSearch(req);
    }

    // ----------------------------------- 결제 영역 -----------------------------------

    // 결제완료 정보 입력( stpay = 1 )
    @PostMapping("/successPay")
    public String successPay(@RequestParam("stNum") String stNum) {

        return hsvc.successPay(stNum);
    }

    // 결제전 정보 입력(stpay = 0)
    @PostMapping("/insertDB")
    public String insertDB(@ModelAttribute TicketingDTO ticket) {

        return hsvc.insertDB(ticket);
    }

    // 내정보 예약 내역 출력
    @PostMapping("/tkList")
    public List<TicketingDTO>   tkList(String tkId) {
        return hsvc.tkList(tkId);
    }

    /* 예약자 목록 내역 출력 */
    @PostMapping("/reservSearch")
    public List<TicketingDTO> reservSearch(@ModelAttribute SearchDTO search, @RequestParam(required = false, defaultValue="pass") String board){
        return hsvc.reservSearch(search,board);
    }

    /* 날짜지난 결제대기 내역 삭제 */
    @PostMapping("/delLastList")
    public String delLastList(){
        return hsvc.delLastList();
    }

    /* 예약자 목록 영역 */
    @PostMapping("/reservlist")
    public List<TicketingDTO> reservlist(){return  hsvc.reservlist();}

    /*환불 요청 내역 불러오기*/
     @PostMapping("/refundList")
    public List<TicketingDTO> refundList(){return  hsvc.refundList();}


    // ----------------------------------- 공지사항 영역 -----------------------------------

    @PostMapping("/noticeList")
    public List<NoticeDTO> noticeList(){
        return adsvc.noticeList();
    }


}
