package com.icia.sdsd.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/* 홈 컨트롤러 홈화면에서 이동하는 모든것들 */
@Controller
@RequiredArgsConstructor
public class HController {

    @GetMapping("/HSearch")
    public String HSearch() {
        return "/hotel/selectList";
    }


    @GetMapping("/사업자정보확인")
    public String 사업자정보확인() {
        return "/사업자정보확인";
    }

    @GetMapping("/개인정보처리방침")
    public String 개인정보처리방침() {
        return "/개인정보처리방침";
    }

    @GetMapping("/숙덕숙덕소개")
    public String 숙덕숙덕소개() {
        return "/숙덕숙덕소개";
    }

    @GetMapping("/customer")
    public String customer() {
        return "/customer";
    }

    /* 결제 페이지 이동 */
    @GetMapping("/payment")
    public String payment(){
        return "/payment";
    }

    /* 1:1 문의 페이지 이동*/
    @GetMapping("/inquiry")
    public String inquiry(){
        return "/inquiry";
    }

    /*로그인 페이지로 이동*/
    @GetMapping("/loginForm")
    public String loginForm(){return "/loginForm";}

    /*공지사항 페이지로 이동*/
    @GetMapping("/noticelist")
    public String noticelist(){
        return "admin/noticeList";
    }

}
