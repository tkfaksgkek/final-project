package com.icia.sdsd.dto;

import lombok.Data;

@Data
public class crawlingDataDTO {

    private String ano;            // 숙소코드
    private String adcno;          // ???
    private String sel_date;    // 숙박시작일
    private String sel_date2;   // 숙박 종료일
    private String link;        // 숙소 링크
    private String alat;        // 숙소 위도
    private String alng;        // 숙소 경도
    private String distance;    // 숙소 거리
    private String image;       // 숙소 이미지
    private String name;        // 숙소 이름
    private String score;       // 숙소 평점
    private String reviewCount; // 리뷰 수
    private String position;    // 숙소 위치
    private String price;       // 숙소 가격

}
