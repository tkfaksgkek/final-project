package com.icia.sdsd.service;


import com.icia.sdsd.dao.OPRepository;
import com.icia.sdsd.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OPService {

    private final OPRepository opreo;

    private final JavaMailSender mailSender;

    private ModelAndView mav;

    private final PasswordEncoder pwEnc;

    private final HttpSession session;



    // 사업자 등록 번호(ID) 중복확인
    public String bnCheck(String soBusnum) {
        String result = "NO";

        // String sobn = opdao.bnCheck(soBusnum);
        Optional<OperatorEntity> sobn = opreo.findBySoBusnum(soBusnum);

        if(sobn.isEmpty()){
            result = "OK";
        }

        System.out.println(result);

        return result;
    }

    // 사업자 이메일 인증번호 전송
    public String emailCheck2(String soEmail) {
        MimeMessage mail = mailSender.createMimeMessage();

        String uuid = UUID.randomUUID().toString().substring(0,8);

        String str = "<h2>안녕하세요. 숙박 예약과 커뮤니티를 함께하는 [숙덕숙덕] 입니다.</h2>"
                + "<p>인증번호는 <b>" + uuid + "</b> 입니다.</p>";
        try {
            mail.setSubject("[숙덕숙덕] 사업자 회원가입 인증번호");
            mail.setText(str, "UTF-8", "html");
            mail.addRecipient(Message.RecipientType.TO, new InternetAddress(soEmail));

            // mailSender.send(mail);

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
        return uuid;
    }

    // 사업자 회원가입
    public ModelAndView soJoin(OperatorDTO operator) {
        mav = new ModelAndView();

        // 사업자 식별번호 생성
        String uuid = UUID.randomUUID().toString().substring(0,8);
        String soNum = uuid;

        operator.setSoNum(soNum);

        operator.setSoPw(pwEnc.encode(operator.getSoPw()));

        OperatorEntity opEntity = OperatorEntity.toEntity(operator);

        opreo.save(opEntity);

        mav.setViewName("/index");
        return mav;
    }

    // 사업자 로그인
    public ModelAndView soLogin(OperatorDTO operator) {
        mav = new ModelAndView();
        String msg="";

        // DB에서 해당 사업자 번호를 가진 사용자 정보를 조회
        Optional<OperatorEntity> operList = opreo.findBySoBusnum(operator.getSoBusnum());

        if (!operList.isEmpty()) {
            String encPw  = operList.get().getSoPw();

            // 암호화된 비밀번호와 입력한 비밀번호 비교.
            if (pwEnc.matches(operator.getSoPw(), encPw)) {

                // 세션에 로그인 정보를 저장
                session.setAttribute("loginNum", operator.getSoBusnum());
                session.setAttribute("soName", operList.get().getSoName());

            } else {
                msg="비밀번호가 틀렸습니다";
            }
        } else {
            msg="아이디가 존재하지 않습니다";
        }
        mav.addObject("msg",msg);
        mav.setViewName("/index");
        return mav;
    }

    // 사업자 등록번호(ID) 찾기
    public String findBn(OperatorDTO operator) {
        System.out.println(operator);

        // String result = opdao.findBn(operator);
        String result = "NO";
        OperatorEntity check = opreo.findBySoNameAndSoPhone(operator.getSoName(), operator.getSoPhone());

        System.out.println(result);
        if (check != null) {
            result = check.getSoBusnum();
        }
        return result;
    }

    // 사업자 비밀번호 변경
    public void changeBn(OperatorDTO operator) {
        System.out.println(operator);
        // opdao.changeBn(operator);
        Optional<OperatorEntity> changeOperator = opreo.findBySoBusnumAndSoPhone(operator.getSoBusnum(), operator.getSoPhone());
        if(changeOperator.isPresent()) {
            OperatorEntity changeOpr = changeOperator.get();
            changeOpr.setSoPw(pwEnc.encode(operator.getSoPw()));
            opreo.save(changeOpr);
        }
    }

    // 사업자 정보 보기
    public ModelAndView soView(String soBusnum) {
        ModelAndView mav = new ModelAndView();

        Optional<OperatorEntity> entity = opreo.findBySoBusnum(soBusnum);

        System.out.println(entity);

        if(entity.isPresent()){
            OperatorDTO operator = new OperatorDTO();
            operator = OperatorDTO.toDTO(entity.get());

            mav.setViewName("/member/opView");
            mav.addObject("view", operator);
        } else {
            mav.setViewName("/index");
        }
        return mav;
    }

    // 사업자 수정 페이지
    public ModelAndView soModiForm(String soNum) {
        mav = new ModelAndView();

        Optional<OperatorEntity> entity = opreo.findById(soNum);

        if(entity.isPresent()){
            OperatorDTO operator = OperatorDTO.toDTO(entity.get());

            mav.setViewName("/member/opModify");
            mav.addObject("modify", operator);
        } else {
            mav.setViewName("/index");
        }

        return mav;
    }

    // 사업자 정보 수정
    public ModelAndView soModify(OperatorDTO operator) {
        mav = new ModelAndView();

        operator.setSoPw(pwEnc.encode(operator.getSoPw()));

        OperatorEntity entity = OperatorEntity.toEntity(operator);

        opreo.save(entity);

        mav.setViewName("redirect:/soView/" + operator.getSoBusnum());
        return mav;
    }

    // 사업자 탈퇴
    public ModelAndView soDelete(String soNum) {
        mav = new ModelAndView();

        try {
            opreo.deleteById(soNum);
            mav.setViewName("/index");
        } catch (Exception e){
            mav.setViewName("/index");
        }

        return mav;
    }

    // 사업자 목록
    public List<OperatorDTO> soList() {
        List<OperatorDTO> operatorList = new ArrayList<>();

        List<OperatorEntity> entityList = opreo.findAll();

        // entityList의 갯수만큼 반복문 실행
        for (OperatorEntity entity : entityList){
            // 한 사람의 정보 : entity
            // entity에서 dto로 변형 : OperatorDTO.toDTO()
            // operatorList 추가 : operatorList.add()
            operatorList.add(OperatorDTO.toDTO(entity));
        }
        return operatorList;
    }

    // 사업자 검색
    public List<OperatorDTO> soSearch(SearchDTO search) {
        System.out.println("키워드"+search);

        // return opdao.soSearch(search);
        List<OperatorDTO> operatorList = new ArrayList<>();
        List<OperatorEntity> entityList = new ArrayList<>();
        if(search.getCategory().equals("SONAME")){
            entityList = opreo.findBySoNameContainingIgnoreCase(search.getKeyword());
        }
        else if(search.getCategory().equals("SOBUSNUM")){
            entityList = opreo.findBySoBusnumContainingIgnoreCase(search.getKeyword());
        }
        else if(search.getCategory().equals("SOCOMPANY")) {
            entityList = opreo.findBySoCompanyContainingIgnoreCase(search.getKeyword());
        }

        for (OperatorEntity entity : entityList){
            operatorList.add(OperatorDTO.toDTO(entity));
        }
        System.out.println(operatorList);
        return operatorList;
    }

    // 사업자 등록 번호(ID)찾기 연락처 찾기
    public String findPhone2(OperatorDTO operator) {
        System.out.println(operator);
        String result = "NO";

        OperatorEntity check = opreo.findBySoNameAndSoPhone(operator.getSoName(), operator.getSoPhone());
        System.out.println(check);

        if (check != null) {
            if (check.getSoPhone().equals(operator.getSoPhone())) {
                result = "OK";
            }
        }
        System.out.println(result);
        return result;
    }

    // 사업자 비밀번호 변경 연락처 찾기
    public String findByBnPhone(OperatorDTO operator) {
        String result = "NO";

        Optional<OperatorEntity> entity = opreo.findBySoBusnumAndSoPhone(operator.getSoBusnum(), operator.getSoPhone());
        System.out.println(entity);

        if (entity.isPresent()) {
            if (entity.get().getSoPhone().equals(operator.getSoPhone())) {
                result = "OK";
            }
        }
        System.out.println(result);

        return result;
    }
}
