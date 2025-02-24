/*
package Quampus.demo.login.service.impl;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import swyp.qampus.login.service.impl.OauthServiceImpl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class OauthServiceImplTest {

    @InjectMocks
    private OauthServiceImpl oauthService;

    @Mock
    private RestTemplate restTemplate;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testKakaoLogout_Success() throws JsonProcessingException {
        // Given: Mock 카카오 API 응답 데이터
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode jsonResponse = objectMapper.createObjectNode();
        jsonResponse.put("id", 123456); // 로그아웃된 사용자 ID

        ResponseEntity<String> responseEntity = new ResponseEntity<>(
                jsonResponse.toString(), HttpStatus.OK);

        // When: RestTemplate이 카카오 API를 호출하면, 위의 응답을 반환하도록 설정
        when(restTemplate.exchange(
                eq("https://kapi.kakao.com/v1/user/logout"),
                eq(HttpMethod.POST),
                any(HttpEntity.class),
                eq(String.class))
        ).thenReturn(responseEntity);

        // Then: 로그아웃이 정상적으로 실행되는지 검증
        assertDoesNotThrow(() -> oauthService.kakaoLogout("mockAccessToken"));

        // Verify: RestTemplate이 정확히 한 번 호출되었는지 검증
        verify(restTemplate, times(1)).exchange(
                eq("https://kapi.kakao.com/v1/user/logout"),
                eq(HttpMethod.POST),
                any(HttpEntity.class),
                eq(String.class)
        );
    }

    @Test
    void testKakaoLogout_Failure() {
        // Given: 카카오 API가 400 Bad Request를 반환하도록 설정
        doThrow(new RuntimeException("카카오 로그아웃 실패")).when(restTemplate).exchange(
                eq("https://kapi.kakao.com/v1/user/logout"),
                eq(HttpMethod.POST),
                any(HttpEntity.class),
                eq(String.class)
        );

        // When & Then: 로그아웃 요청 시 예외 발생 검증
        Exception exception = assertThrows(RuntimeException.class, () -> oauthService.kakaoLogout("mockAccessToken"));
        assertEquals("카카오 로그아웃 실패", exception.getMessage());

        // Verify: RestTemplate이 정확히 한 번 호출되었는지 확인
        verify(restTemplate, times(1)).exchange(
                eq("https://kapi.kakao.com/v1/user/logout"),
                eq(HttpMethod.POST),
                any(HttpEntity.class),
                eq(String.class)
        );
    }
}*/
