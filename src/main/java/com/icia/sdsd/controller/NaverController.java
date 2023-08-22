package com.icia.sdsd.controller;

import com.icia.sdsd.dto.MemberEntity;
import com.icia.sdsd.dto.MsgEntity;
import com.icia.sdsd.dto.NaverDTO;
import com.icia.sdsd.service.MemberService;
import com.icia.sdsd.service.NaverService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@RestController
@RequiredArgsConstructor
@RequestMapping("naver")
public class NaverController {

    private final NaverService naverService;

    private final MemberService memberService;

    private final HttpSession session;

    @GetMapping("/callback")
    public ResponseEntity<MsgEntity> callback(HttpServletRequest request) throws Exception {
        NaverDTO naverInfo = naverService.getNaverInfo(request.getParameter("code"));

        String email = naverInfo.getEmail(); // 네이버에서 얻어온 이메일
        String smNickname = naverInfo.getNickname(); // 카카오에서 얻어온 닉네임
        System.out.println(naverInfo);

        // 이메일로 가입여부 확인
        String checkId = memberService.idCheck(email);

        // 가입되어 있지 않은 경우
        if (checkId.equals("OK")) {

            // 닉네임 중복여부 확인
            String checkNick = memberService.NicknameCheck(smNickname);
            // 닉네임이 중복되지 않는 경우
            if(checkNick.equals("OK")) {
                // 카카오 정보 데이터베이스에 저장
                memberService.saveNaverMember(naverInfo);
                // 세션에 이메일과 닉네임 저장
                session.invalidate();
                session.setAttribute("loginId", email);
                session.setAttribute("nickName", smNickname);
                return ResponseEntity.status(HttpStatus.FOUND) // 302 Found 상태 코드
                        .header(HttpHeaders.LOCATION, "/") // 리다이렉트할 경로 설정
                        .build();
                // 닉네임이 중복되는 경우
            }else {
                // 랜덤 닉네임 생성
                String newNick=memberService.createNick();
                naverInfo.setNickname(newNick);
                // 카카오 정보 데이터베이스에 저장
                memberService.saveNaverMember(naverInfo);
                // 세션에 이메일과 닉네임 저장
                session.invalidate();
                session.setAttribute("loginId", email);
                session.setAttribute("nickName", newNick);

                // 오류 메시지 생성
                String errorMessage = "닉네임이 중복됩니다.\n 랜덤 닉네임 ["+newNick+"]이 생성되었습니다.\n 변경을 원하시면 회원정보에서 수정해 주세요" ;

                // 오류 메시지를 리다이렉트 URL에 파라미터로 추가하여 전달
                String redirectURL = "/?error=" + URLEncoder.encode(errorMessage, StandardCharsets.UTF_8);

                return ResponseEntity.status(HttpStatus.FOUND)
                        .header(HttpHeaders.LOCATION, redirectURL)
                        .build();
            }
            // 가입되어 있는 경우 로그인
        }else {
            MemberEntity member = memberService.getMemberByEmail(email);
            session.invalidate();
            session.setAttribute("loginId", email);
            session.setAttribute("nickName", member.getSmNickname());
            return ResponseEntity.status(HttpStatus.FOUND) // 302 Found 상태 코드
                    .header(HttpHeaders.LOCATION, "/") // 리다이렉트할 경로 설정
                    .build();
        }
    }
}
