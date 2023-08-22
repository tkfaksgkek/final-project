package com.icia.sdsd.controller;

import com.icia.sdsd.dto.MemberDTO;
import com.icia.sdsd.dto.OperatorDTO;
import com.icia.sdsd.service.OPService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;

@Controller
@RequiredArgsConstructor
public class OPController {

    private final OPService opsv;

    private final HttpSession session;

    @PostMapping("/soJoin")
    public ModelAndView soJoin(@ModelAttribute OperatorDTO operator){
        System.out.println(operator);
        return opsv.soJoin(operator);
    }

    @PostMapping("/soLogin")
    public ModelAndView soLogin(@ModelAttribute OperatorDTO operator){
        System.out.println(operator);
        return opsv.soLogin(operator);
    }

    @PostMapping("/findBn")
    @ResponseBody
    public String findBn(@ModelAttribute OperatorDTO operator) {
        System.out.println(operator);
        return opsv.findBn(operator);
    }

    @PostMapping("/changeBn")
    @ResponseBody
    public void changeBn(@ModelAttribute OperatorDTO operator) {
        System.out.println(operator);
        opsv.changeBn(operator);
    }

    @GetMapping("/soView/{soBusnum}")
    public ModelAndView soView(@PathVariable String soBusnum){
        return opsv.soView(soBusnum);
    }

    @GetMapping("/soModiForm/{soNum}")
    public ModelAndView soModiForm(@PathVariable String soNum){
        return opsv.soModiForm(soNum);
    }

    @PostMapping("/soModify")
    public ModelAndView soModify(@ModelAttribute OperatorDTO operator){
        System.out.println(operator);
        return opsv.soModify(operator);
    }

    @GetMapping("/soDelete/{soNum}")
    public ModelAndView soDelete(@PathVariable String soNum){
        return opsv.soDelete(soNum);
    }

}
