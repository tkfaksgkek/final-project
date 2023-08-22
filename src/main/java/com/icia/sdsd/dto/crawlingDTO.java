package com.icia.sdsd.dto;

import lombok.Data;

@Data
public class crawlingDTO {
    private String areaCode;    // 지역코드
    private String typeCode;    // 숙소타입코드
    private String ano;         // 숙소코드
    private String adcno;       // ???
    private String sel_date;    // 숙박 시작일
    private String sel_date2;   // 숙박 종료일
    private String totalPrice;  // 총 가격
    private String hotelName;   // 숙소 이름
    private String roomInfo;    // 숙소 정보
    private String Keyword;     // 검색 키워드
    private int minPrice;    // 최소가격
    private int maxPrice;    // 최대가격
    private String tabs;     // 탭페이지
}
