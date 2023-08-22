package com.icia.sdsd.dto;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.sql.Date;

@Data
@Entity
@Table(name="GENERALBOARD")
@SequenceGenerator(name = "GENERALBOARD_SEQ_GENERATOR", sequenceName = "GB_SEQ", allocationSize = 1)
public class GBoardEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "GENERALBOARD_SEQ_GENERATOR")
    private int gbNum;

    @Column
    private int gbCode;

    @Column
    private String gbWriter;

    @Column
    private String gbWriterId;

    @Column
    private String gbTitle;

    @Column
    private String gbContent;

    @Column
    @CreationTimestamp
    private Date gbDate;

    @Column
    @UpdateTimestamp
    private Date gbUpdateDate;

    @Column
    private int gbHit;

    @Column
    private String gbFileName1;

    @Column
    private String gbFileName2;

    @Column
    private String gbFileName3;

    public static GBoardEntity toEntity(GBoardDTO gBoardDTO){
        GBoardEntity entity = new GBoardEntity();

        entity.setGbNum(gBoardDTO.getGbNum());
        entity.setGbCode(gBoardDTO.getGbCode());
        entity.setGbWriter(gBoardDTO.getGbWriter());
        entity.setGbWriterId(gBoardDTO.getGbWriterId());
        entity.setGbTitle(gBoardDTO.getGbTitle());
        entity.setGbContent(gBoardDTO.getGbContent());
        entity.setGbDate(gBoardDTO.getGbDate());
        entity.setGbUpdateDate(gBoardDTO.getGbUpdateDate());
        entity.setGbHit(gBoardDTO.getGbHit());
        entity.setGbFileName1(gBoardDTO.getGbFileName1());
        entity.setGbFileName2(gBoardDTO.getGbFileName2());
        entity.setGbFileName3(gBoardDTO.getGbFileName3());

        return entity;

    }
}
