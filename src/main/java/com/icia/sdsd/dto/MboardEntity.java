package com.icia.sdsd.dto;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Date;

@Data
@Entity
@Table(name = "MARKETINGBOARD")
@SequenceGenerator(name ="MARKETINGBOARD_SEQ_GENERATOR", sequenceName = "MB_SEQ", allocationSize = 1)
public class MboardEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "MARKETINGBOARD_SEQ_GENERATOR")
    private int mbNum;          // 게시글 번호(마케팅 보드)

    @Column
    private int mbrNum;         // 게시글 번호(홍보 요청 보드)

    @Column
    private String mbWriter;    // 기업체명

    @Column
    private String mbTitle;     // 게시글 제목

    @Column
    private String mbContent;   // 게시글 내용

    @Column
    @CreationTimestamp
    private Date mbDate;        // 게시글 작성 일시


    @Column
    private String mbUrl;       // 숙소 상세정보 주소

    @Column
    private String mbImg1;
    @Column
    private String mbImg2;
    @Column
    private String mbImg3;
    @Column
    private String mbHotelName;
    @Column
    private String mbHotelAddr;



    public static MboardEntity toEntity(MBoardDTO mBoard){
        MboardEntity entity = new MboardEntity();

        entity.setMbNum(mBoard.getMbNum());
        entity.setMbrNum(mBoard.getMbrNum());
        entity.setMbWriter(mBoard.getMbWriter());
        entity.setMbTitle(mBoard.getMbTitle());
        entity.setMbContent(mBoard.getMbContent());
        entity.setMbDate(mBoard.getMbDate());
        entity.setMbUrl(mBoard.getMbUrl());
        entity.setMbImg1(mBoard.getMbImg1());
        entity.setMbImg2(mBoard.getMbImg2());
        entity.setMbImg3(mBoard.getMbImg3());
        entity.setMbHotelName(mBoard.getMbHotelName());
        entity.setMbHotelAddr(mBoard.getMbHotelAddr());
        return entity;
    }
}
