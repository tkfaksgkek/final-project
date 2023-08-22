package com.icia.sdsd.controller;

import com.icia.sdsd.dto.GBoardDTO;
import com.icia.sdsd.service.GBService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;


/* 자유게시판 컨트롤러 */
@Controller
@RequiredArgsConstructor
public class GBController {

    private final GBService gbsvc;


    @GetMapping("/GBList")
    public String GBList() {
        return "/generalBoard/GBList";
    }

    @GetMapping("/writeForm")
    public String writeForm() {
        return "/generalBoard/GBWrite";
    }

    @PostMapping("/GBWrite")
    public String GBWrite(@ModelAttribute GBoardDTO gBoardDTO) {
        gbsvc.GBWrite(gBoardDTO);
        return "redirect:/GBList";
    }

    // 자유게시판 상세보기
    @GetMapping("/gbView/{gbNum}")
    public ModelAndView gbView(@PathVariable int gbNum){
        return gbsvc.gbView(gbNum);
    }


    // gbModiForm : 게시판수정폼 페이지
    @GetMapping("/gbModiForm/{gbNum}")
    public ModelAndView gbModiForm(@PathVariable int gbNum){
        return gbsvc.gbModiForm(gbNum);
    }

    // gbModify : 게시판 수정 저장
    @PostMapping("/gbModify")
    public String  gbModify(@ModelAttribute GBoardDTO gBoardDTO){
        gbsvc.gbModify(gBoardDTO);
        return "redirect:/GBList";
    }

    @GetMapping("/gbDelete/{GbNum}")
    public ModelAndView gbDelete(@PathVariable int GbNum){
        return gbsvc.gbDelete(GbNum);
    }

}

