package com.icia.sdsd.controller;

import com.icia.sdsd.dto.MarketingRequestDTO;
import com.icia.sdsd.dto.crawlingDTO;
import com.icia.sdsd.service.MBService;
import com.icia.sdsd.service.hotelService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

/*마케팅 게시판 컨트롤러*/
@Controller
@RequiredArgsConstructor
public class MBController {

    private final MBService mbsvc;

    private final hotelService hsvc;

    private ModelAndView mav=new ModelAndView();

    //홍보 게시판 이동
    @GetMapping("/MBList")
    public String MBList() {
        return "/maketingBoard/MBList";
    }

    // 홍보 게시판 글쓰기(관리자용)
    @GetMapping("/MBWriteForm/{ano}")
    public ModelAndView MBWriteForm(@PathVariable String ano){
        crawlingDTO data = new crawlingDTO();
        data.setAno(ano);
        data.setAdcno("1");
        mav=hsvc.detail(data);
        mav.setViewName("/maketingBoard/MBWrite");
        return mav;
    }

    // 홍보게시판 글 등록(관리자)
    @PostMapping("/MBWrite")
    public ModelAndView MBWrite(@ModelAttribute MarketingRequestDTO mRequest){
        return mbsvc.MBWrite(mRequest);
    }

    // 홍보요청 요청 게시판 상세보기
    @GetMapping("/mrView/{mrNum}")
    public ModelAndView mrView(@PathVariable int mrNum){ return mbsvc.mrView(mrNum); }


    // 홍보 요청 게시판 이동
    @GetMapping("/MRequest")
    public String MRequest(){
        return "/maketingBoard/MRequest";
    }

    // 홍보 요청 게시판 글쓰기
    @PostMapping("/mrWrite")
    public ModelAndView mrWrite(@ModelAttribute MarketingRequestDTO mRequest){
        return mbsvc.mrWrite(mRequest);
    }

    // 홍보 게시판 글쓰기(사업자)
    @GetMapping(value={"/MRWriteForm/{ano}","/MRWriteForm"})
    public ModelAndView MRWriteForm(@PathVariable(required = false) String ano){
        mav.clear();
        mav.addObject("ano",ano);
        mav.setViewName("/maketingBoard/MRWrite");
        return mav;
    }

}
