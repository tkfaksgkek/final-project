package com.icia.sdsd.dto;

import lombok.Data;
import org.apache.ibatis.type.Alias;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Date;

@Data
@Alias("mBoard")
public class MBoardDTO {

    private int mbNum;          // 게시글 번호(마케팅 보드)
    private int mbrNum;         // 게시글 번호(홍보 요청 보드)
    private String mbWriter;    // 기업체명

    private String mbTitle;     // 게시글 제목
    private String mbContent;   // 게시글 내용
    private Date mbDate;        // 게시글 작성 일시

    private String mbUrl;       // 숙소 상세정보 주소

    private String mbImg1;
    private String mbImg2;
    private String mbImg3;
    private String mbHotelName;
    private String mbHotelAddr;



    public static MBoardDTO toDTO(MboardEntity entity){
        MBoardDTO mBoard = new MBoardDTO();

        mBoard.setMbNum(entity.getMbNum());
        mBoard.setMbrNum(entity.getMbrNum());
        mBoard.setMbWriter(entity.getMbWriter());
        mBoard.setMbTitle(entity.getMbTitle());
        mBoard.setMbContent(entity.getMbContent());
        mBoard.setMbDate(entity.getMbDate());

        mBoard.setMbUrl(entity.getMbUrl());
        mBoard.setMbImg1(entity.getMbImg1());
        mBoard.setMbImg2(entity.getMbImg2());
        mBoard.setMbImg3(entity.getMbImg3());
        mBoard.setMbHotelName(entity.getMbHotelName());
        mBoard.setMbHotelAddr(entity.getMbHotelAddr());
        return mBoard;
    }


}
