package com.icia.sdsd.dto;

import lombok.Data;
import org.apache.ibatis.type.Alias;

import java.sql.Date;

@Data
@Alias("notice")
public class NoticeDTO {

    private int snNum;
    private String snTitle;
    private String snContent;
    private String snWriter;
    private Date snDate;
    private Date snUpdateDate;


    public static NoticeDTO toDTO(NoticeEntity entity){
        NoticeDTO notice = new NoticeDTO();

        notice.setSnNum(entity.getSnNum());
        notice.setSnTitle(entity.getSnTitle());
        notice.setSnContent(entity.getSnContent());
        notice.setSnWriter(entity.getSnWriter());
        notice.setSnDate(entity.getSnDate());
        notice.setSnUpdateDate(entity.getSnUpdateDate());

        return notice;
    }
}
