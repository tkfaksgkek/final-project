package com.icia.sdsd.dto;



import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "MARKETINGREQUEST")
@SequenceGenerator(name ="MARKETINGREQUEST_SEQ_GENERATOR", sequenceName = "MAREQ_SEQ", allocationSize = 1)
public class MarketingRequestEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "MARKETINGREQUEST_SEQ_GENERATOR")
    private int mrNum;          // 홍보요청 게시판 글 번호

    @Column
    private String mrAno;       // 숙소 일련번호

    @Column
    private String mrTitle;     // 숙소 홍보 내용 제목

    @Column
    private String mrContent;    // 숙소 홍보 내용

    @Column
    private String mrSoName;     // 숙소 홍보 요청 작성자

    @Column
    private int mrResponse;      // 숙소 홍보 등록여부 (0,1)


    public static MarketingRequestEntity toEntity(MarketingRequestDTO mRequest){
        MarketingRequestEntity entity = new MarketingRequestEntity();

        entity.setMrNum(mRequest.getMrNum());
        entity.setMrAno(mRequest.getMrAno());
        entity.setMrTitle(mRequest.getMrTitle());
        entity.setMrContent(mRequest.getMrContent());
        entity.setMrSoName(mRequest.getMrSoName());
        entity.setMrResponse(mRequest.getMrResponse());

        return entity;
    }
}
