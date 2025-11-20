package com.example.myselectshop.config;

import java.time.Duration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {

    @Bean
    public RestClient restClient() {
        // RestClient 로 외부 API 호출 시 일정 시간이 지나도 응답이 없을 때
        // 무한 대기 상태 방지를 위해 강제 종료 설정
        JdkClientHttpRequestFactory requestFactory = new JdkClientHttpRequestFactory();
        requestFactory.setReadTimeout(Duration.ofMillis(5000)); // 5초

        return RestClient.builder().requestFactory(requestFactory).build();
    }
}
