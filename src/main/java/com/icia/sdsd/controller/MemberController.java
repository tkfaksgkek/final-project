package com.icia.sdsd.controller;

import com.icia.sdsd.dto.MemberDTO;
import com.icia.sdsd.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;


/* Member 컨트롤러 */
@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService msvc;

    private final HttpSession session;

    @GetMapping({"/index","/"})
    public String index() {
        return "/index";
    }

    @PostMapping("/mJoin")
    public ModelAndView mJoin(@ModelAttribute MemberDTO member) {
        return msvc.mJoin(member);
    }

    @PostMapping("/mLogin")
    public ModelAndView mLogin(@ModelAttribute MemberDTO member) {
        return msvc.mLogin(member);
    }

    @GetMapping("/mLogout")
    public String logout() {
        session.invalidate();
        return "redirect:/index";
    }

    @PostMapping("/findId")
    @ResponseBody
    public String findId(@ModelAttribute MemberDTO member) {
        System.out.println(member);
        return msvc.findId(member);
    }

    @PostMapping("/changePw")
    @ResponseBody
    public void changePw(@ModelAttribute MemberDTO member) {
        System.out.println(member);
        msvc.changePw(member);
    }

    @GetMapping({"/mView/{smId}", "/mView/{smId}/{tabs}"})
    public ModelAndView mView2(@PathVariable String smId,@PathVariable(required = false) String tabs) {
        System.out.println(smId);
        return msvc.mView2(smId,tabs);
    }

    @GetMapping("/mModiForm/{smId}")
    public ModelAndView mModiForm(@PathVariable String smId) {
        return msvc.mModiForm(smId);
    }

    @PostMapping("/mModify")
    public ModelAndView mModify(@ModelAttribute MemberDTO member) {
        System.out.println(member);
        return msvc.mModify(member);
    }

    @GetMapping("/mDelete/{smId}")
    public ModelAndView mDelete(@PathVariable String smId) {
        return msvc.mDelete(smId);
    }

    @GetMapping("/list")
    public String list() {
        return "/member/list";
    }

    @PostMapping("/changeNickname")
    @ResponseBody
    public void changeNickname(@ModelAttribute MemberDTO member) {
        System.out.println(member);
        msvc.changeNickname(member);
    }

}