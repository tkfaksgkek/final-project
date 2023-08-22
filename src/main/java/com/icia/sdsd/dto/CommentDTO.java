package com.icia.sdsd.dto;

import lombok.Data;
import org.apache.ibatis.type.Alias;

import java.sql.Date;

@Data
@Alias("cmt")
public class CommentDTO {

    private int gcNum;
    private int gcBNum;
    private String gcWriter;
    private String gcWriterId;
    private String gcContent;
    private Date gcDate;
    private Date gcUpdateDate;

    public static CommentDTO toDTO(CommentEntity cmtentity) {
        CommentDTO cmt = new CommentDTO();

        cmt.setGcNum(cmtentity.getGcNum());
        cmt.setGcBNum(cmtentity.getGcBNum());
        cmt.setGcWriter(cmtentity.getGcWriter());
        cmt.setGcWriterId(cmtentity.getGcWriterId());
        cmt.setGcContent(cmtentity.getGcContent());
        cmt.setGcDate(cmtentity.getGcDate());
        cmt.setGcUpdateDate(cmtentity.getGcUpdateDate());

        return cmt;
    }
}
