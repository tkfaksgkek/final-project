package com.icia.sdsd.dto;

import lombok.Data;

@Data
public class crawlingViewDTO {

    private String ano;             // 숙소코드
    private String adcno;           // ???
    private String sel_date;    // 숙박시작일
    private String sel_date2;   // 숙박 종료일
    private String name;            // 숙소 이름
    private String score;           // 숙소 평점
    private String tel;             // 숙소 전화
    private String address;         // 숙소 주소
    private String comment;         // 숙소 멘트

    private String defaultInfo;   // 하단 기본 정보
    private String sellerInfo;        // 하단 판매자 정보
    private String tabs;     // 탭페이지


}
