package com.icia.sdsd.dto;

import lombok.Data;
import org.apache.ibatis.type.Alias;
import org.springframework.web.multipart.MultipartFile;

@Data
@Alias("member")
public class MemberDTO {
    private String smId;			// 아이디
    private String smPw;			// 비밀번호
    private String smName;		// 이름
    private String smNickname;      // 닉네임
    private String smBirth;		// 생년월일
    private String smGender;		// 성별
    private String smEmail;		// 이메일
    private String smPhone;		// 연락처
    private String smAddr;		// 주소

    private MultipartFile smProfile;		// 업로드 파일
    private String smProfileName;		// 업로드 파일이름

    public static MemberDTO toDTO(MemberEntity entity){
        MemberDTO member = new MemberDTO();

        member.setSmId(entity.getSmId());
        member.setSmPw(entity.getSmPw());
        member.setSmName(entity.getSmName());
        member.setSmNickname(entity.getSmNickname());
        member.setSmBirth(entity.getSmBirth());
        member.setSmGender(entity.getSmGender());
        member.setSmEmail(entity.getSmEmail());
        member.setSmPhone(entity.getSmPhone());
        member.setSmAddr(entity.getSmAddr());
        member.setSmProfileName(entity.getSmProfileName());

        return member;
    }

}
