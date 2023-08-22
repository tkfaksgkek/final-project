package com.icia.sdsd.dto;

import lombok.Data;
import org.apache.ibatis.type.Alias;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Date;

@Data
@Alias("fav")
public class favDTO {
    private int faNum;
    private String faHotelName;     // 숙소이름
    private String faUrl;            // 숙소링크
    private String faImage;            // 숙소이미지
    private String faCode;          // 숙소코드
    private String faPrice;            // 숙소가격
    private String faScore;            // 숙소별점
    private String faAddr;            // 숙소주소
    private String faLoginId;        // 아이디
    private Date faDate;




    public static favDTO toDTO(favEntity entity){
        favDTO fav = new favDTO();

        fav.setFaNum(entity.getFaNum());
        fav.setFaHotelName(entity.getFaHotelName());
        fav.setFaUrl(entity.getFaUrl());
        fav.setFaImage(entity.getFaImage());
        fav.setFaCode(entity.getFaCode());
        fav.setFaPrice(entity.getFaPrice());
        fav.setFaScore(entity.getFaScore());
        fav.setFaAddr(entity.getFaAddr());
        fav.setFaLoginId(entity.getFaLoginId());
        fav.setFaDate(entity.getFaDate());



        return fav;
    }

}
