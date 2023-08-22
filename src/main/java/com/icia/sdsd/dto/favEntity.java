package com.icia.sdsd.dto;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Date;

@Data
@Entity
@Table(name="SDFAV")
@SequenceGenerator(name="SDFAV_SEQ_GENERATOR", sequenceName = "SDFAV_SEQ", allocationSize = 1)
public class favEntity {
    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator ="SDFAV_SEQ_GENERATOR")
    private int faNum;
    @Column
    private String faHotelName;     // 숙소이름
    @Column
    private String faUrl;            // 숙소링크
    @Column
    private String faImage;            // 숙소이미지
    @Column
    private String faCode;          // 숙소코드
    @Column
    private String faPrice;            // 숙소가격
    @Column
    private String faScore;            // 숙소별점
    @Column
    private String faAddr;            // 숙소주소
    @Column
    private String faLoginId;        // 아이디
    @Column
    @CreationTimestamp
    private Date faDate;

    public static favEntity toEntity(favDTO fav){
        favEntity entity=new favEntity();
        entity.setFaNum(fav.getFaNum());
        entity.setFaHotelName(fav.getFaHotelName());
        entity.setFaUrl(fav.getFaUrl());
        entity.setFaImage(fav.getFaImage());
        entity.setFaCode(fav.getFaCode());
        entity.setFaPrice(fav.getFaPrice());
        entity.setFaScore(fav.getFaScore());
        entity.setFaAddr(fav.getFaAddr());
        entity.setFaLoginId(fav.getFaLoginId());
        entity.setFaDate(fav.getFaDate());
        return entity;
    }
}
