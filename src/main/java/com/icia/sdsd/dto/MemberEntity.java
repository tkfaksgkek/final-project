package com.icia.sdsd.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name="SDMEMBER")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberEntity {

    @Id
    @Column
    private String smId;

    @Column
    private String smPw;

    @Column
    private String smName;

    @Column
    private String smNickname;

    @Column
    private String smBirth;

    @Column
    private String smGender;

    @Column
    private String smEmail;

    @Column
    private String smPhone;

    @Column
    private String smAddr;

    @Column
    private String smProfileName;

    public static MemberEntity toEntity(MemberDTO member){
        MemberEntity entity = new MemberEntity();

        entity.setSmId(member.getSmId());
        entity.setSmPw(member.getSmPw());
        entity.setSmName(member.getSmName());
        entity.setSmNickname(member.getSmNickname());
        entity.setSmBirth(member.getSmBirth());
        entity.setSmGender(member.getSmGender());
        entity.setSmEmail(member.getSmEmail());
        entity.setSmPhone(member.getSmPhone());
        entity.setSmAddr(member.getSmAddr());
        entity.setSmProfileName(member.getSmProfileName());

        return entity;
    }
}
