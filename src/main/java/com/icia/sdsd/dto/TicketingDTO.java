package com.icia.sdsd.dto;


import lombok.Data;
import org.apache.ibatis.type.Alias;

import javax.persistence.Column;
import java.util.Date;


@Data
@Alias("ticketing")
public class TicketingDTO {
    private String  stNum;          // 예약번호
    private String  stId;           // 예약자 ID
    private String  stCode;         // 예약 숙소 일련번호
    private String  stRoomInfo;     // 예약 숙소 명칭+ 방
    private String  stStartDay;     // 숙박 시작 날짜
    private String  stEndDay;       // 숙박 종료 날짜
    private int     stPay;          // 결제 여부
    private int     stAmount;       // 결제 금액
    private String  stName;         // 예약자 이름
    private String  stPhone;        // 예약자 연락처
    private String  stVist;         // 예약자 방문 수단
    private String  stUrl;          // 예약 숙소 상세보기 주소
    private String  stImg;          // 예약 숙소 이미지
    private Date stDate;            // 예약 날짜

    public static TicketingDTO toDTO(TicketingEntity entity){
        TicketingDTO ticket = new TicketingDTO();

        ticket.setStNum(entity.getStNum());
        ticket.setStStartDay(entity.getStStartDay());
        ticket.setStId(entity.getStId());
        ticket.setStEndDay(entity.getStEndDay());
        ticket.setStCode(entity.getStCode());
        ticket.setStRoomInfo(entity.getStRoomInfo());
        ticket.setStPay(entity.getStPay());
        ticket.setStAmount(entity.getStAmount());
        ticket.setStName(entity.getStName());
        ticket.setStPhone(entity.getStPhone());
        ticket.setStVist(entity.getStVist());
        ticket.setStDate(entity.getStDate());
        ticket.setStUrl(entity.getStUrl());
        ticket.setStImg(entity.getStImg());

        return ticket;
    }


}
