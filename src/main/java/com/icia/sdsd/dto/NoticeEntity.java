package com.icia.sdsd.dto;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.sql.Date;

@Data
@Entity
@Table(name="SDNOTICE")
@SequenceGenerator(name = "SDNOTICE_SEQ_GENERATOR", sequenceName = "SN_SEQ",allocationSize = 1)
public class NoticeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SDNOTICE_SEQ_GENERATOR")
    private int snNum;

    @Column
    private String snTitle;

    @Column
    private String snContent;

    @Column
    private String snWriter;

    @Column
    @CreationTimestamp
    private Date snDate;

    @Column
    @UpdateTimestamp
    private Date snUpdateDate;

    public static NoticeEntity toEntity(NoticeDTO notice){
        NoticeEntity entity = new NoticeEntity();

        entity.setSnNum(notice.getSnNum());
        entity.setSnTitle(notice.getSnTitle());
        entity.setSnContent(notice.getSnContent());
        entity.setSnWriter(notice.getSnWriter());
        entity.setSnDate(notice.getSnDate());
        entity.setSnUpdateDate(notice.getSnUpdateDate());

        return entity;
    }


}
