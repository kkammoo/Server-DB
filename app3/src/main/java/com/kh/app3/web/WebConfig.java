package com.kh.app3.web;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
  
  //cors허용하기위한 글로벌 설정
  @Override
  public void addCorsMappings(CorsRegistry registry) {
    registry.addMapping("/api/**")            //요청url
        .allowedOrigins("http://192.168.0.11:5501","http://localhost:5501")    //요청 client
        .allowedMethods("*")                            //모든 메소드
        .maxAge(3000);                                  //캐쉬시간
  }
}
