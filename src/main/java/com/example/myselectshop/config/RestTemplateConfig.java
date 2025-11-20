package com.example.myselectshop.config;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

@Configuration
public class RestTemplateConfig  {
    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder restTemplateBuilder) {
        // RestTemplate 으로 외부 API 호출 시 일정 시간이 지나도 응답이 없을 때
        // 무한 대기 상태 방지를 위해 강제 종료 설정
        return restTemplateBuilder
            .connectTimeout(Duration.ofMillis(5000))
            .readTimeout(Duration.ofMillis(5000)) // 5초
            .build();
    }
}