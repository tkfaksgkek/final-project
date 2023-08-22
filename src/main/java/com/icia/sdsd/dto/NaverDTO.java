package com.icia.sdsd.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class NaverDTO {

    private String id;
    private String name;
    private String email;
    private String nickname;
    private String profile_image;
    private String gender;
    private String birthyear;
    private String birthday;
    private String mobile;
}
