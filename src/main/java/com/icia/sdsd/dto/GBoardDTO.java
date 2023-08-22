package com.icia.sdsd.dto;

import lombok.Data;
import org.apache.ibatis.type.Alias;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Date;

@Data
@Alias("gBoard")
public class GBoardDTO {

    private int gbNum;
    private int gbCode;
    private String gbWriter;
    private String gbWriterId;
    private String gbTitle;
    private String gbContent;
    private Date gbDate;
    private Date gbUpdateDate;
    private int gbHit;

    private MultipartFile gbFile1;
    private MultipartFile gbFile2;
    private MultipartFile gbFile3;

    private String gbFileName1;
    private String gbFileName2;
    private String gbFileName3;

    public static GBoardDTO toDTO(GBoardEntity entity){
        GBoardDTO gBoard = new GBoardDTO();

        gBoard.setGbNum(entity.getGbNum());
        gBoard.setGbCode(entity.getGbCode());
        gBoard.setGbWriter(entity.getGbWriter());
        gBoard.setGbWriterId(entity.getGbWriterId());
        gBoard.setGbTitle(entity.getGbTitle());
        gBoard.setGbContent(entity.getGbContent());
        gBoard.setGbDate(entity.getGbDate());
        gBoard.setGbUpdateDate(entity.getGbUpdateDate());
        gBoard.setGbHit(entity.getGbHit());
        gBoard.setGbFileName1(entity.getGbFileName1());
        gBoard.setGbFileName2(entity.getGbFileName2());
        gBoard.setGbFileName3(entity.getGbFileName3());

        return gBoard;
    }
}
