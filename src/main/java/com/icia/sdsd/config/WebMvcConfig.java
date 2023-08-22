package com.icia.sdsd.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    //web root가 아닌 외부 경로에 있는 리소스를 url로 불러올 수 있도록 설정
    //현재 localhost:9092/SpringBoot/SDSD/src/main/resources/static/boardUpload//1234.jpg
    //로 접속하면 D:/SpringBoot/SDSD/src/main/resources/static/boardUpload/1234.jpg 파일을 불러온다.
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/SpringBoot/SDSD/src/main/resources/static/boardUpload/**")
                .addResourceLocations("file:///D:/SpringBoot/SDSD/src/main/resources/static/boardUpload/");
    }
}
