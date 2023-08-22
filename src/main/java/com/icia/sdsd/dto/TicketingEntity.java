package com.icia.sdsd.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;


@Data
@Entity
@Table(name="SDTICKETING")
public class TicketingEntity {

    @Id
    @Column
    private String  stNum;

    @Column
    private String  stId;

    @Column
    private String  stCode;

    @Column
    private String  stRoomInfo;

    @Column
    private String stStartDay;

    @Column
    private String    stEndDay;

    @Column
    private int     stPay;

    @Column
    private int     stAmount;

    @Column
    private String  stName;

    @Column
    private String  stPhone;

    @Column
    private String  stVist;

    @Column
    private String  stUrl;

    @Column
    private String  stImg;

    @CreationTimestamp
    @Column
    private Date stDate;


    public static TicketingEntity toEntity(TicketingDTO ticket){
        TicketingEntity entity = new TicketingEntity();

        entity.setStNum(ticket.getStNum());
        entity.setStId(ticket.getStId());
        entity.setStStartDay(ticket.getStStartDay());
        entity.setStEndDay(ticket.getStEndDay());
        entity.setStCode(ticket.getStCode());
        entity.setStRoomInfo(ticket.getStRoomInfo());
        entity.setStPay(ticket.getStPay());
        entity.setStAmount(ticket.getStAmount());
        entity.setStName(ticket.getStName());
        entity.setStPhone(ticket.getStPhone());
        entity.setStVist(ticket.getStVist());
        entity.setStDate(ticket.getStDate());
        entity.setStUrl(ticket.getStUrl());
        entity.setStImg(ticket.getStImg());
        return entity;
    }



}
