package com.icia.sdsd.controller;

import com.icia.sdsd.dto.ReviewDTO;
import com.icia.sdsd.dto.crawlingDTO;
import com.icia.sdsd.service.hotelService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;


@Controller
@RequiredArgsConstructor
public class hotelController {

    private final hotelService hsvc;

    private ModelAndView mav=new ModelAndView();

    // 호텔 검색 페이지 이동
    @GetMapping("/selectList")
    public String selectList(){
        return "/hotel/selectList";
    }

    // 선택한 정보로 호텔 상세 페이지 이동
    @GetMapping("/detail")
    public ModelAndView detail(crawlingDTO data){
        mav=hsvc.detail(data);
        return mav;
    }

    // 선택한 정보로 예약 페이지 이동
    @GetMapping("/reserve")
    public ModelAndView reserve(crawlingDTO data){
        mav=hsvc.reserve(data);
        return mav;
    }

    // 미결제내역 재결제
    @GetMapping("/rePay/{stNum}")
    public ModelAndView rePay(@PathVariable String stNum){
        System.out.println(stNum);
        return hsvc.rePay(stNum);
    }

    // (결제할때) 미결제내역 삭제
    @GetMapping("/ticketingDel/{stNum}")
    public String ticketingDel(@PathVariable String stNum){
        return hsvc.ticketingDel(stNum);
    }

    // 예약취소( 결제가 된것은 환불 요청으로 미결제 건은 바로 지우기)
    @GetMapping("/deletePay/{stNum}")
    public ModelAndView deletepay(@PathVariable String stNum){
        return hsvc.deletePay(stNum);
    }
    // 호텔 리뷰
    @PostMapping ("/reviewWrite")
    public ModelAndView reviewWrite(@ModelAttribute ReviewDTO review){return hsvc.reviewWrite(review);}

}
