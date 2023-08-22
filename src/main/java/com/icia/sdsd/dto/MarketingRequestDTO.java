package com.icia.sdsd.dto;

import lombok.Data;
import org.apache.ibatis.type.Alias;

@Data
@Alias("marketingRequest")
public class MarketingRequestDTO {

    private int mrNum;          // 홍보요청 게시판 글 번호
    private String mrAno;       // 숙소 일련번호
    private String mrTitle;     // 숙소 홍보 내용 제목
    private String mrContent;    // 숙소 홍보 내용
    private String mrSoName;     // 숙소 홍보 요청 작성자
    private int mrResponse;      // 숙소 홍보 등록여부 (0,1)


    public static MarketingRequestDTO toDTO(MarketingRequestEntity entity){
        MarketingRequestDTO mRequest = new MarketingRequestDTO();

        mRequest.setMrNum(entity.getMrNum());
        mRequest.setMrAno(entity.getMrAno());
        mRequest.setMrTitle(entity.getMrTitle());
        mRequest.setMrContent(entity.getMrContent());
        mRequest.setMrSoName(entity.getMrSoName());
        mRequest.setMrResponse(entity.getMrResponse());
        return mRequest;
    }
}
