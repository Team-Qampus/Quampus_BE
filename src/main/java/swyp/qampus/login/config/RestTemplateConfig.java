package swyp.qampus.login.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import javax.inject.Qualifier;
import java.util.ArrayList;
import java.util.List;

@Configuration
public class RestTemplateConfig {

    @Bean
    @Primary
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        // 메시지 컨버터 리스트 생성
        List<HttpMessageConverter<?>> messageConverters = new ArrayList<>();
        // x-www-form-urlencoded 방식의 요청을 지원하는 FormHttpMessageConverter 추가
        messageConverters.add(new FormHttpMessageConverter());
        // RestTemplate에 메시지 컨버터 설정 적용
        restTemplate.setMessageConverters(messageConverters);

        return restTemplate;
    }
}
