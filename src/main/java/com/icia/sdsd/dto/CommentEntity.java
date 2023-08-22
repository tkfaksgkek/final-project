package com.icia.sdsd.dto;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.sql.Date;


@Data
@Entity
@Table(name = "GENERALCOMMENT")
@SequenceGenerator(name="GENERALCOMMENT_SEQ_GENERATOR", sequenceName = "CMT_SEQ", allocationSize = 1)
public class CommentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "GENERALCOMMENT_SEQ_GENERATOR")
    private int gcNum;

    @Column
    private int gcBNum;

    @Column
    private String gcWriter;

    @Column
    private String gcWriterId;

    @Column
    private String gcContent;

    @Column
    @CreationTimestamp
    private Date gcDate;

    @Column
    @UpdateTimestamp
    private Date gcUpdateDate;

    public static CommentEntity toEntity(CommentDTO cmt){
        CommentEntity cmtentity = new CommentEntity();

        cmtentity.setGcNum(cmt.getGcNum());
        cmtentity.setGcBNum(cmt.getGcBNum());
        cmtentity.setGcWriter(cmt.getGcWriter());
        cmtentity.setGcWriterId(cmt.getGcWriterId());
        cmtentity.setGcContent(cmt.getGcContent());
        cmtentity.setGcDate(cmt.getGcDate());
        cmtentity.setGcUpdateDate(cmt.getGcUpdateDate());

        return cmtentity;

    }
}

