package com.icia.sdsd.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class KakaoDTO {

    private long id;
    private String email;
    private String nickname;
    private String profileImageUrl;
    private String gender;
    private String birthday;
}
