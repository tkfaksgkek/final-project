package com.icia.sdsd.dto;

import lombok.Data;
import org.apache.ibatis.type.Alias;

@Data
@Alias("hotel")
public class HotelDTO {
    private String shNum;           // 숙소 소유주 일련번호
    private String shCode;          // 숙소 일련번호
    private String shName;          // 숙소 이름
    private Number shPrice;         // 가격
    private String shAddr;          // 숙소 주소
    private String shLatitude;      // 위도
    private String shLongitude;     // 경도
    private String shRoomtype;      // 숙소 방타입
    private Number shHeadCount;     // 숙소 최대 인원
    private String shKind;          // 숙소 분류(모텔 호텔 펜션)
    private String shTheme;         // 테마
    private String shInfo;          // 숙소정보
    private String shPicture1;      // 숙소 사진1
    private String shPicture2;      // 숙소 사진2
    private String shPicture3;      // 숙소 사진3
    private String shPicture4;      // 숙소 사진4
    private String shPicture5;      // 숙소 사진5
    private String shPicture6;      // 숙소 사진6
}
