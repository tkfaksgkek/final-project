package com.icia.sdsd.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.icia.sdsd.dao.MemberRepository;
import com.icia.sdsd.dto.*;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository mrepo;

    private final JavaMailSender mailSender;

    private ModelAndView mav;

    private final PasswordEncoder pwEnc;

    private final HttpSession session;

    private final AmazonS3Client amazonS3Client;
    private String bucket="sdsdfile";


    // 아이디 중복 확인
    public String idCheck(String smId) {
        String result = "NO";

        Optional<MemberEntity> entity = mrepo.findById(smId);

        if(entity.isEmpty()){
            result = "OK";
        }
        System.out.println(result);
        return result;

    }

    // 이메일 인증번호 전송
    public String emailCheck(String smEmail) {
        MimeMessage mail = mailSender.createMimeMessage();

        String uuid = UUID.randomUUID().toString().substring(0, 8);

        String str = "<body class=\"clean-body u_body\" style=\"margin: 0;padding: 0;-webkit-text-size-adjust: 100%;background-color: #e7e7e7;color: #000000\">\n" +
                "<style type=\"text/css\">\n" +
                "@media only screen and (min-width: 620px){\n" +
                ".u-row {width: 600px !important;}\n" +
                ".u-row .u-col {vertical-align: top;}\n" +
                ".u-row .u-col-100 {width: 600px !important;}}\n" +
                "@media (max-width: 620px) {\n" +
                ".u-row-container {max-width: 100% !important;padding-left: 0px !important;padding-right: 0px !important;}\n" +
                ".u-row .u-col {min-width: 320px !important;max-width: 100% !important;display: block !important;}\n" +
                ".u-row {width: 100% !important;}\n" +
                ".u-col {width: 100% !important;}\n" +
                ".u-col > div {margin: 0 auto;}}\n" +
                "body {margin: 0;padding: 0;}\n" +
                "table,tr,td {vertical-align: top;border-collapse: collapse;}\n" +
                "p {margin: 0;}\n" +
                ".ie-container table,.mso-container table {table-layout: fixed;}\n" +
                "* {line-height: inherit;}\n" +
                "a[x-apple-data-detectors='true'] {color: inherit !important;text-decoration: none !important;}\n" +
                "table, td { color: #000000; } @media (max-width: 480px) { #u_content_image_1 .v-container-padding-padding { padding: 0px 0px 0px 30px !important; } \n" +
                "#u_content_heading_1 .v-container-padding-padding { padding: 30px 10px 20px 30px !important; } \n" +
                "#u_content_text_7 .v-container-padding-padding { padding: 10px 30px 20px 0px !important; } \n" +
                "#u_content_text_3 .v-container-padding-padding { padding: 10px 30px 20px 0px !important; } \n" +
                "#u_content_text_4 .v-container-padding-padding { padding: 10px 15px 0px !important; } \n" +
                "#u_content_text_4 .v-text-align { text-align: center !important; } }\n" +
                "</style>\n" +
                "<table style=\"border-collapse: collapse;table-layout: fixed;border-spacing: 0;mso-table-lspace: 0pt;mso-table-rspace: 0pt;vertical-align: top;min-width: 320px;Margin: 0 auto;background-color: #e7e7e7;width:100%\" cellpadding=\"0\" cellspacing=\"0\">\n" +
                "<tbody>\n" +
                "<tr style=\"vertical-align: top\">\n" +
                "<td style=\"word-break: break-word;border-collapse: collapse !important;vertical-align: top\"><div class=\"u-row-container\" style=\"padding: 70px 0px 0px;background-color: #ffffff\">\n" +
                "<div class=\"u-row\" style=\"margin: 0 auto;min-width: 320px;max-width: 600px;overflow-wrap: break-word;word-wrap: break-word;word-break: break-word;background-color: transparent;\">\n" +
                "<div style=\"border-collapse: collapse;display: table;width: 100%;height: 100%;background-color: transparent;\">\n" +
                "<div class=\"u-col u-col-100\" style=\"max-width: 320px;min-width: 600px;display: table-cell;vertical-align: top;\">\n" +
                "<div style=\"background-color: #ffffff;height: 100%;width: 100% !important;\">\n" +
                "<div style=\"box-sizing: border-box; height: 100%; padding: 0px;border-top: 1px solid #ced4d9;border-left: 1px solid #ced4d9;border-right: 1px solid #ced4d9;border-bottom: 1px solid #ced4d9;\">  \n" +
                "<table id=\"u_content_image_1\" style=\"font-family:arial,helvetica,sans-serif;\" role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" border=\"0\">\n" +
                "<tbody>\n" +
                "<tr>\n" +
                "<td class=\"v-container-padding-padding\" style=\"overflow-wrap:break-word;word-break:break-word;padding:20px 0px 20px 50px;font-family:arial,helvetica,sans-serif;\" align=\"left\">\n" +
                "<table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\">\n" +
                "<tr>\n" +
                "<td class=\"v-text-align\" style=\"padding-right: 0px;padding-left: 0px;\" align=\"justify\">\n" +
                "<img align=\"justify\" border=\"0\" src=\"https://sdsdfile.s3.ap-northeast-2.amazonaws.com/img/logo1.png\"\n" +
                "alt=\"image\" title=\"image\" style=\"outline: none;text-decoration: none;-ms-interpolation-mode: bicubic;clear: both;display: inline-block !important;border: none;height: auto;float: none;width: 100%;max-width: 220px;\" width=\"220\"/>      \n" +
                "</td>\n" +
                "</tr>\n" +
                "</table>\n" +
                "</td>\n" +
                "</tr>\n" +
                "</tbody>\n" +
                "</table>\n" +
                "<table id=\"u_content_heading_1\" style=\"font-family:arial,helvetica,sans-serif;\" role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" border=\"0\">\n" +
                "<tbody>\n" +
                "<tr>\n" +
                "<td class=\"v-container-padding-padding\" style=\"overflow-wrap:break-word;word-break:break-word;padding:30px 0px;font-family:arial,helvetica,sans-serif;\" align=\"left\">\n" +
                "<h3 class=\"v-text-align\" style=\"margin: 0px; line-height: 110%; text-align: center; word-wrap: break-word; font-size: 18px; font-weight: 700;\"><span data-metadata=\"&lt;!--(figmeta)eyJmaWxlS2V5IjoidkNvS3ZmUVRpVUhrZVdaaEFTeGtkWiIsInBhc3RlSUQiOjEyMzgxMDY2MDAsImRhdGFUeXBlIjoic2NlbmUifQo=(/figmeta)--&gt;\" style=\"line-height: 36.3px;\"></span>\n" +
                "<div>안녕하세요. <br/>숙박 예약과 커뮤니티를 함께하는 <strong style=\"color: #275da0;\">숙덕숙덕</strong> 입니다.</div></h3>\n" +
                "</td>\n" +
                "</tr>\n" +
                "</tbody>\n" +
                "</table>\n" +
                "<table id=\"u_content_text_7\" style=\"font-family:arial,helvetica,sans-serif;\" role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" border=\"0\">\n" +
                "<tbody>\n" +
                "<tr>\n" +
                "<td class=\"v-container-padding-padding\" style=\"overflow-wrap:break-word;word-break:break-word;padding:20px 0px;font-family:arial,helvetica,sans-serif;\" align=\"left\">\n" +
                "<div class=\"v-text-align\" style=\"font-size: 18px; line-height: 140%; text-align: center; word-wrap: break-word;\">\n" +
                "<div>\n" +
                "<div><span style=\"color: #275da0; line-height: 25.2px;\">숙덕숙덕</span> 회원가입 인증번호</div>\n" +
                "</div>\n" +
                "<div> </div>\n" +
                "<div>인증번호는 <strong style=\"font-size: 25px; color: #275da0;\">&nbsp;&nbsp;" + uuid + "&nbsp;&nbsp;</strong> 입니다.</div>\n" +
                "</div>\n" +
                "</td>\n" +
                "</tr>\n" +
                "</tbody>\n" +
                "</table>\n" +
                "<table id=\"u_content_text_3\" style=\"font-family:arial,helvetica,sans-serif;\" role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" border=\"0\">\n" +
                "<tbody>\n" +
                "<tr>\n" +
                "<td class=\"v-container-padding-padding\" style=\"overflow-wrap:break-word;word-break:break-word;padding:30px;font-family:arial,helvetica,sans-serif;\" align=\"left\">\n" +
                "<div class=\"v-text-align\" style=\"font-size: 13px; line-height: 140%; text-align: center; word-wrap: break-word;\">\n" +
                "<p style=\"line-height: 140%;\">본 이메일 인증은 <span style=\"color: #275da0; line-height: 19.6px;\">숙덕숙덕 <span style=\"color: #000000; line-height: 19.6px;\">회원가입을 위한</span></span></p>\n" +
                "<p style=\"line-height: 140%;\"><span style=\"color: #275da0; line-height: 19.6px;\"><span style=\"color: #000000; line-height: 19.6px;\">필수 사항입니다. 인증번호를 확인 후 홈페이지에서</span></span></p>\n" +
                "<p style=\"line-height: 140%;\"><span style=\"color: #275da0; line-height: 19.6px;\"><span style=\"color: #000000; line-height: 19.6px;\">이메일 인증</span> <span style=\"color: #000000; line-height: 19.6px;\">입력창에 입력하시고 남은 회원가입 절차를 완료하여 주시기 바랍니다.</span></span></p>\n" +
                "</div>\n" +
                "</td>\n" +
                "</tr>\n" +
                "</tbody>\n" +
                "</table>\n" +
                "</div>\n" +
                "</div>\n" +
                "</div>\n" +
                "</div>\n" +
                "</div>\n" +
                "</div>\n" +
                "<div class=\"u-row-container\" style=\"padding: 0px 0px 70px;background-color: #ffffff\">\n" +
                "<div class=\"u-row\" style=\"margin: 0 auto;min-width: 320px;max-width: 600px;overflow-wrap: break-word;word-wrap: break-word;word-break: break-word;background-color: transparent;\">\n" +
                "<div style=\"border-collapse: collapse;display: table;width: 100%;height: 100%;background-color: transparent;\">\n" +
                "<div class=\"u-col u-col-100\" style=\"max-width: 320px;min-width: 600px;display: table-cell;vertical-align: top;\">\n" +
                "<div style=\"background-color: #275da0;height: 100%;width: 100% !important;border-radius: 0px;-webkit-border-radius: 0px; -moz-border-radius: 0px;\">\n" +
                "<div style=\"box-sizing: border-box; height: 100%; padding: 0px;border-top: 0px solid transparent;border-left: 0px solid transparent;border-right: 0px solid transparent;border-bottom: 0px solid transparent;border-radius: 0px;-webkit-border-radius: 0px; -moz-border-radius: 0px;\">  \n" +
                "<table id=\"u_content_text_4\" style=\"font-family:arial,helvetica,sans-serif;\" role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" border=\"0\">\n" +
                "<tbody>\n" +
                "<tr>\n" +
                "<td class=\"v-container-padding-padding\" style=\"overflow-wrap:break-word;word-break:break-word;padding:20px 30px;font-family:arial,helvetica,sans-serif;\" align=\"left\">\n" +
                "<div class=\"v-text-align\" style=\"font-size: 12px; color: #ecf0f1; line-height: 180%; text-align: justify; word-wrap: break-word;\">\n" +
                "<p style=\"line-height: 180%;\">본 메일은 발신전용 메일이므로 문의 및 회신하실 경우 답변되지 않습니다.<br />메일 내용에 대해 궁금한 사항이 있으시면 <span style=\"color: #fbeeb8; line-height: 25.2px;\">숙덕숙덕 고객센터(1:1문의)</span>로 문의하여 주시기 바랍니다.<br /><span style=\"color: #ced4d9; line-height: 25.2px;\">ⓒ 숙덕숙덕-자고 갈래? 2023.</span></p>\n" +
                "</div>\n" +
                "</td>\n" +
                "</tr>\n" +
                "</tbody>\n" +
                "</table>\n" +
                "</div>\n" +
                "</div>\n" +
                "</div>\n" +
                "</div>\n" +
                "</div>\n" +
                "</div>  \n" +
                "</td>\n" +
                "</tr>\n" +
                "</tbody>\n" +
                "</table>\n" +
                "</body>";
        try {
            mail.setSubject("[숙덕숙덕] 회원가입 인증번호");
            mail.setText(str, "UTF-8", "html");
            mail.addRecipient(Message.RecipientType.TO, new InternetAddress(smEmail));

            mailSender.send(mail);

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }

        return uuid;
    }

    // 닉네임 중복 체크
    public String NicknameCheck(String smNickname) {
        String result = "NO";

        // String nick = mdao.NicknameCheck(smNickname);
        List<MemberEntity> nick = mrepo.findBySmNickname(smNickname);
        System.out.println(nick);

        if(nick.isEmpty()){
            result = "OK";
        }

        System.out.println(result);
        return result;
    }

    // 회원가입
    public ModelAndView mJoin(MemberDTO member) {
        mav = new ModelAndView();

        // [1] 파일 업로드
        MultipartFile mProfile = member.getSmProfile();

        if (!mProfile.isEmpty()){
            String uuid = UUID.randomUUID().toString().substring(0,8);
            String originalFileName = mProfile.getOriginalFilename();
            String mProfileName = uuid + "_" + originalFileName;

            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentType(mProfile.getContentType());
            objectMetadata.setContentLength(mProfile.getSize());

            String key = "profile/" + mProfileName;

            try {
                InputStream fileStream = mProfile.getInputStream();
                amazonS3Client.putObject(new PutObjectRequest(bucket,key,fileStream,objectMetadata).withCannedAcl(CannedAccessControlList.PublicRead));;    //파일 저장

            } catch (IOException e) {
                e.printStackTrace();
            }
            member.setSmProfileName(amazonS3Client.getUrl(bucket,key).toString());
        }

        // [2] 비밀번호 암호화
        member.setSmPw(pwEnc.encode(member.getSmPw()));

        // DTO → Entity로 변형
        MemberEntity mEntity = MemberEntity.toEntity(member);

        // 가입
        mrepo.save(mEntity);

        mav.setViewName("redirect:/index");
        return mav;
    }

    // 회원 로그인
    public ModelAndView mLogin(MemberDTO member) {
        mav = new ModelAndView();
        String msg="";

        Optional<MemberEntity> entity = mrepo.findById(member.getSmId());
        if(entity.isPresent()){
            if(pwEnc.matches(member.getSmPw(), entity.get().getSmPw())){
                member = new MemberDTO();
                member = MemberDTO.toDTO(entity.get());
                session.invalidate();
                session.setAttribute("loginId", member.getSmId());
                session.setAttribute("nickName", member.getSmNickname());
                session.setAttribute("loginProfile", member.getSmProfileName());

            }else{
                msg="비밀번호가 틀렸습니다";
            }
        } else {
            msg="아이디가 존재하지 않습니다";
        }
        mav.addObject("msg",msg);
        mav.setViewName("redirect:/index");
        return mav;
    }

    // 테스트 로그인
    public String testLogin(MemberDTO member) {
        String msg="OK";

        Optional<MemberEntity> entity = mrepo.findById(member.getSmId());
        if(entity.isPresent()){
            if(pwEnc.matches(member.getSmPw(), entity.get().getSmPw())){
                member = new MemberDTO();
                member = MemberDTO.toDTO(entity.get());
                // 일반로그인 사업자 로그인의 중복이 되지 않게 session.invalidate
                session.invalidate();
                session.setAttribute("loginId", member.getSmId());
                session.setAttribute("nickName", member.getSmNickname());
                session.setAttribute("loginProfile", member.getSmProfileName());

            }else{
                msg="비밀번호가 틀렸습니다";
            }
        } else {
            msg="아이디가 존재하지 않습니다";
        }
        return msg;
    }

    // 아이디 찾기
    public String findId(MemberDTO member) {
        System.out.println(member);
        // String result = mdao.findId(member);
        String result="NO";
        MemberEntity check=mrepo.findBySmNameAndSmPhone(member.getSmName(), member.getSmPhone());

        if (check != null) {
            result =check.getSmId() ;
        }
        System.out.println(result);
        return result;
    }

    // 비밀번호 변경
    public void changePw(MemberDTO member) {
        System.out.println(member);
        // mdao.changePw(member);
        Optional<MemberEntity> changeMember=mrepo.findBySmIdAndSmPhone(member.getSmId(), member.getSmPhone());
        if(changeMember.isPresent()){
            MemberEntity changeMem=changeMember.get();
            changeMem.setSmPw(pwEnc.encode(member.getSmPw()));
            mrepo.save(changeMem);
        }
    }

    // 회원정보보기
    public ModelAndView mView2(String smId,String tabs) {
        mav = new ModelAndView();

        Optional<MemberEntity> entity = mrepo.findById(smId);

        if(entity.isPresent()){
            MemberDTO member = MemberDTO.toDTO(entity.get());

            mav.setViewName("/member/memberView");
            mav.addObject("view", member);
            mav.addObject("tabs",tabs);
        } else {
            mav.setViewName("/index");
        }

        return mav;

    }

    // 회원정보보기
    public ModelAndView mView(String smId) {
        mav = new ModelAndView();

        Optional<MemberEntity> entity = mrepo.findById(smId);

        if(entity.isPresent()){
            MemberDTO member = MemberDTO.toDTO(entity.get());

            mav.setViewName("/member/memberView");
            mav.addObject("view", member);
        } else {
            mav.setViewName("/index");
        }

        return mav;

    }

    // 회원수정 페이지
    public ModelAndView mModiForm(String smId) {
        mav = new ModelAndView();

        MemberDTO member = (MemberDTO)mView(smId).getModel().get("view");

        mav.setViewName("/member/memModify");
        mav.addObject("modify", member);

        return mav;
    }

    // 회원수정
    public ModelAndView mModify(MemberDTO member) {
        mav = new ModelAndView();

        // 파일 삭제
        String ogFileName = member.getSmProfileName();
        if(!ogFileName.isEmpty()) {
            String key = ogFileName.substring(49);
            System.out.println(key);
            amazonS3Client.deleteObject(bucket, key);    //저장된 파일 삭제
        }

        // [1] 파일 업로드
        MultipartFile mProfile = member.getSmProfile();

        if (!mProfile.isEmpty()){
            String uuid = UUID.randomUUID().toString().substring(0,8);
            String originalFileName = mProfile.getOriginalFilename();
            String mProfileName = uuid + "_" + originalFileName;

            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentType(mProfile.getContentType());
            objectMetadata.setContentLength(mProfile.getSize());

            String key = "profile/" + mProfileName;

            try {
                InputStream fileStream = mProfile.getInputStream();
                amazonS3Client.putObject(new PutObjectRequest(bucket,key,fileStream,objectMetadata).withCannedAcl(CannedAccessControlList.PublicRead));;    //파일 저장

            } catch (IOException e) {
                e.printStackTrace();
            }
            member.setSmProfileName(amazonS3Client.getUrl(bucket,key).toString());
        }


        // [2] 비밀번호 암호화
        member.setSmPw(pwEnc.encode(member.getSmPw()));

        // DTO → Entity로 변형
        MemberEntity mEntity = MemberEntity.toEntity(member);

        // 수정
        mrepo.save(mEntity);

        mav.setViewName("redirect:/mView/" + member.getSmId());
        return mav;

    }

    // 회원삭제
    public ModelAndView mDelete(String smId) {
        mav = new ModelAndView();
        Optional<MemberEntity> member = mrepo.findById(smId);
        String delFileName = member.get().getSmProfileName();

        try {
            mrepo.deleteById(smId);
            session.invalidate();
            if(!delFileName.isEmpty()) {
                String key = delFileName.substring(49);
            System.out.println(key);
            amazonS3Client.deleteObject(bucket, key);    //저장된 파일 삭제
            }
            mav.setViewName("redirect:/index");
        } catch (Exception e){
            mav.setViewName("redirect:/mView/" + smId);
        }

        return mav;

    }


    // 회원목록
    public List<MemberDTO> mList() {
        List<MemberDTO> memberList = new ArrayList<>();

        List<MemberEntity> entityList = mrepo.findAll();

        // entityList의 갯수만큼 반복문 실행
        for (MemberEntity entity : entityList){
            // 한 사람의 정보 : entity
            // entity에서 dto로 변형 : MemberDTO.toDTO()
            // memberList에 추가 : memberList.add()
            memberList.add(MemberDTO.toDTO(entity));
        }
        return memberList;

    }

    // 회원검색
    public List<MemberDTO> mSearch(SearchDTO search) {
        System.out.println("키워드"+search);

        // return mdao.mSearch(search);
        List<MemberDTO> memberList = new ArrayList<>();
        List<MemberEntity> entityList= new ArrayList<>();
        if(search.getCategory().equals("SMID")) {
            entityList = mrepo.findBySmIdContainingIgnoreCase(search.getKeyword());
        }
        else if(search.getCategory().equals("SMNiCKNAME")) {
            entityList = mrepo.findBySmNicknameContainingIgnoreCase(search.getKeyword());
        }

        for (MemberEntity entity : entityList){
            memberList.add(MemberDTO.toDTO(entity));
        }
        System.out.println(memberList);
        return memberList;
    }

    // 닉네임 변경
    public void changeNickname(MemberDTO member) {
        System.out.println(member);
        // mdao.changeNickname(member);
        // 기존의 닉네임 변경 로직
        Optional<MemberEntity> changeNickname = mrepo.findById(member.getSmId());
        if (changeNickname.isPresent()) {
            MemberEntity changeNick = changeNickname.get();

            // 닉네임 변경을 위한 수정 로직
            changeNick.setSmNickname(member.getSmNickname()); // 예시로 'newNickname'라고 가정
            mrepo.save(changeNick);
            session.setAttribute("nickName", member.getSmNickname());
            System.out.println("닉네임이 변경되었습니다.");
        } else {
            System.out.println("회원을 찾을 수 없습니다.");
        }
    }

    // 일반회원 id찾기 휴대폰 번호 찾기
    public String findPhone(MemberDTO member) {
        String result = "NO";

        MemberEntity check = mrepo.findBySmNameAndSmPhone(member.getSmName(), member.getSmPhone());
        System.out.println(check);

        if (check != null) {
            if (check.getSmPhone().equals(member.getSmPhone())) {
                result = "OK";
            }
        }
        System.out.println(result);
        return result;
    }

    // 일반회원 비밀번호 변경 휴대폰 번호 찾기
    public String findbyIdPhone(MemberDTO member) {
        String result = "NO";

        Optional<MemberEntity> entity = mrepo.findById(member.getSmId());
        System.out.println(entity);

        if (entity.isPresent()) {
            if (entity.get().getSmPhone().equals(member.getSmPhone())) {
                result = "OK";
            }
        }
        System.out.println(result);
        return result;
    }

    // 카카오 계정 저장
    public void saveKakaoMember(KakaoDTO kakaoInfo) {
        System.out.println(kakaoInfo);
//        String kakaoEmail = kakaoInfo.getEmail();
//        // 이미 해당 이메일로 저장된 정보가 있는지 확인
//        Optional<MemberEntity> entity = mrepo.findById(kakaoInfo.getEmail());
//

            try {
                String profileImageUrl = kakaoInfo.getProfileImageUrl();
//                // profileImageUrl 변수에 저장된 URL 문자열을 사용하여 새로운 URL 객체를 생성
//                URL url = new URL(profileImageUrl);
//                String fileName = "kakao_profile_" + kakaoEmail + ".jpg"; // 파일 이름
//                Path filePath = Paths.get(path.toString(), fileName); // 경로 문자열을 Path로 변환
//                System.out.println(fileName);
//
//                // 주어진 URL에서 입력 스트림을 연다.
//                // 이 입력 스트림은 웹상의 리소스를 읽어오기 위해 사용 됨
//                InputStream in = url.openStream();
//                // Files.copy() 메서드를 사용하여 입력 스트림에서 읽은 내용을 지정된 파일 경로(filePath)로 복사
//                // StandardCopyOption.REPLACE_EXISTING 옵션은 이미 존재하는 파일을 덮어쓰도록 지정하는 옵션
//                Files.copy(in, filePath, StandardCopyOption.REPLACE_EXISTING);
//                // 이전에 열었던 입력 스트림을 닫기
//                in.close();

                // 성별 정보에 따라 '여자' 또는 '남자'로 저장
                String gender = kakaoInfo.getGender();
                String genderText = "선택안함"; // 기본 값
                if (gender.equals("female")) {
                    genderText = "여자";
                } else if (gender.equals("male")) {
                    genderText = "남자";
                }

                // 카카오 계정에서 불러온 정보를 SDMEMBER 테이블에 저장
                MemberEntity member = MemberEntity.builder()
                        .smId(kakaoInfo.getEmail())
                        .smName(kakaoInfo.getNickname())
                        .smNickname(kakaoInfo.getNickname())
                        .smBirth(kakaoInfo.getBirthday())
                        .smGender(genderText)
                        .smEmail(kakaoInfo.getEmail())
                        .smProfileName(profileImageUrl) // Path 객체로 저장
                        .build();

                mrepo.save(member);
            } catch (Exception e) {
                // 예외 처리
                e.printStackTrace();
            }
        }


    // 네이버 계정 저장
    public void saveNaverMember(NaverDTO naverInfo) {
        System.out.println(naverInfo);
//        String naverEmail = naverInfo.getEmail();
//        // 이미 해당 이메일로 저장된 정보가 있는지 확인
//        Optional<MemberEntity> entity = mrepo.findById(naverInfo.getEmail());
//

            try {
                String profile_image = naverInfo.getProfile_image();
                // profileImageUrl 변수에 저장된 URL 문자열을 사용하여 새로운 URL 객체를 생성
//                URL url = new URL(profile_image);
//                String fileName = "naver_profile_" + naverEmail + ".jpg"; // 파일 이름
//                Path filePath = Paths.get(path.toString(), fileName); // 경로 문자열을 Path로 변환
//                System.out.println(fileName);
//
//                // 주어진 URL에서 입력 스트림을 연다.
//                // 이 입력 스트림은 웹상의 리소스를 읽어오기 위해 사용 됨
//                InputStream in = url.openStream();
//                // Files.copy() 메서드를 사용하여 입력 스트림에서 읽은 내용을 지정된 파일 경로(filePath)로 복사
//                // StandardCopyOption.REPLACE_EXISTING 옵션은 이미 존재하는 파일을 덮어쓰도록 지정하는 옵션
//                Files.copy(in, filePath, StandardCopyOption.REPLACE_EXISTING);
//                // 이전에 열었던 입력 스트림을 닫기
//                in.close();

                // 성별 정보에 따라 '여자' 또는 '남자'로 저장
                String gender = naverInfo.getGender();
                String genderText = "선택안함"; // 기본 값
                if (gender.equals("F")) {
                    genderText = "여자";
                } else if (gender.equals("M")) {
                    genderText = "남자";
                }

                // 네이버 계정에서 불러온 정보를 SDMEMBER 테이블에 저장
                MemberEntity member = MemberEntity.builder()
                        .smId(naverInfo.getEmail())
                        .smName(naverInfo.getName())
                        .smNickname(naverInfo.getNickname())
                        .smBirth(naverInfo.getBirthyear() + "-" + naverInfo.getBirthday())
                        .smGender(genderText)
                        .smEmail(naverInfo.getEmail())
                        .smPhone(naverInfo.getMobile())
                        .smProfileName(profile_image) // Path 객체로 저장
                        .build();

                mrepo.save(member);
            } catch (Exception e) {
                // 예외 처리
                e.printStackTrace();
            }
        }


    // 이메일을 통해 회원정보 찾기
    public MemberEntity getMemberByEmail(String email) {
        return mrepo.findBySmEmail(email);
    }

    // 랜덤 닉네임 생성기
    public String createNick() throws Exception
    {
        URL url = null;
        String readLine = null;
        StringBuilder buffer = null;
        BufferedReader bufferedReader = null;
        BufferedWriter bufferedWriter = null;
        HttpURLConnection urlConnection = null;

        int connTimeout = 5000;
        int readTimeout = 3000;

        String apiUrl = "https://nickname.hwanmoo.kr/?format=json&count=1";    // 각자 상황에 맞는 IP & url 사용

        try
        {
            url = new URL(apiUrl);
            urlConnection = (HttpURLConnection)url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setConnectTimeout(connTimeout);
            urlConnection.setReadTimeout(readTimeout);
            urlConnection.setRequestProperty("Accept", "application/json;");

            buffer = new StringBuilder();
            if(urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK)
            {
                bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(),"UTF-8"));
                while((readLine = bufferedReader.readLine()) != null)
                {
                    buffer.append(readLine).append("\n");
                }
            }
            else
            {
                buffer.append("code : ");
                buffer.append(urlConnection.getResponseCode()).append("\n");
                buffer.append("message : ");
                buffer.append(urlConnection.getResponseMessage()).append("\n");
            }
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        finally
        {
            try
            {
                if (bufferedWriter != null) { bufferedWriter.close(); }
                if (bufferedReader != null) { bufferedReader.close(); }
            }
            catch(Exception ex)
            {
                ex.printStackTrace();
            }
        }
        System.out.println("결과 : " + buffer.toString());
        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject = (JSONObject)jsonParser.parse(buffer.toString());
        JSONArray array = (JSONArray) jsonObject.get("words");
        System.out.println(array.get(0));
        return (String) array.get(0);
    }

}
