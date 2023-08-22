package com.icia.sdsd.controller;

import com.icia.sdsd.dto.NoticeDTO;
import com.icia.sdsd.service.AdminService;
import com.icia.sdsd.service.hotelService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequiredArgsConstructor
public class adminController {
    private final hotelService hsvc;

    private final AdminService asvc;

    /*예약목록(reservation) 페이지 이동*/
    @GetMapping("/reservation")
    public String reservation(){return "admin/reservation";}

    /*일반 회원목록(admin member) 페이지 이동*/
    @GetMapping("/AMList")
    public String AMList(){return "admin/AMList";}

    /*사업자 회원목록(admin operator) 페이지 이동*/
    @GetMapping("/AOList")
    public String AOList(){return "admin/AOList";}

    /*환불요청 페이지 이동*/
    @GetMapping("/ARList")
    public String ARList(){return "admin/ARList";}

    /*공지사항관리 페이지 이동*/
    @GetMapping("/notice")
    public String notice(){return "admin/notice";}

    /*홍보요청확인 페이지 이동*/
    @GetMapping("/request")
    public String request(){return "admin/request";}


    /*환불 승인*/
    @GetMapping("/requestPay/{stNum}")
    public ModelAndView requestPay(@PathVariable("stNum") String stNum){
        return hsvc.requestPay(stNum);
    }
    /* 공지사항 작성 */
    @PostMapping("/noticeWrite")
    public ModelAndView noticeWrite(@ModelAttribute NoticeDTO notice){
        return asvc.noticeWrite(notice);
    }

    //홈페이지 에서 공지사항 더보기로 이동
    @GetMapping("/snView/{snNum}")
    public ModelAndView snView(@PathVariable("snNum") int snNum){
        return asvc.snView(snNum);
    }

    // 공지사항 수정 폼으로 이동
    @GetMapping("/noticeModiForm/{snNum}")
    public ModelAndView noticeModiForm(@PathVariable("snNum") int snNum){
        return asvc.noticeModiForm(snNum);
    }

    // 공지사항 수정하기
    @PostMapping("/noticeModify")
    public ModelAndView noticeModify(@ModelAttribute NoticeDTO notice){
        return asvc.noticeModify(notice);
    }

    // 공지사항 삭제하기
    @GetMapping("/noticeDelete/{snNum}")
    public String noticeDelete(@PathVariable int snNum){
        return asvc.noticeDelete(snNum);
    }

    /* 관리자 회원삭제*/
    @GetMapping("/amDelete/{smId}")
    public ModelAndView amDelete(@PathVariable String smId) {
        return asvc.amDelete(smId);
    }

    /* 관리자 사업자 삭제*/
    @GetMapping("/aoDelete/{soNum}")
    public ModelAndView aoDelete(@PathVariable String soNum){
        return asvc.aoDelete(soNum);
    }

}
