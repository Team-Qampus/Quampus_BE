/*
package Quampus.demo.login.service;

import swyp.qampus.login.config.data.RedisCustomServiceImpl;
import swyp.qampus.login.dto.UserRequestDTO;
import swyp.qampus.login.entity.User;
import swyp.qampus.login.repository.UserRepository;
import swyp.qampus.login.service.CompleteSignupService;
import swyp.qampus.login.util.JWTUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class CompleteSignupServiceTest {

    @Mock
    private RedisCustomServiceImpl redisCustomService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private JWTUtil jwtUtil;

    @InjectMocks
    private CompleteSignupService completeSignupService;

    private ObjectMapper objectMapper = new ObjectMapper();

    private User tempUser;
    private String tempUserJson;
    private final String email = "test@example.com";
    private final String redisKey = "tempUser:" + email;
    private final Long userId = 1L;

    @BeforeEach
    public void setup() throws JsonProcessingException {
        MockitoAnnotations.openMocks(this);
        // 임시 사용자 객체 생성 (universityName, major는 null)
        tempUser = User.builder()
                .email(email)
                .name("Test User")
                .major(null)
                .build();
        // 임시 객체를 JSON으로 변환
        tempUserJson = objectMapper.writeValueAsString(tempUser);
    }

    */
/**
     * Redis에 임시 사용자 정보가 존재하는 경우, 추가 정보를 병합하여 DB 저장 및 최종 JWT 토큰 생성하는 경우를 테스트합니다.
     *//*

    @Test
    public void testCompleteSignup_Success() {
        // given: Redis에서 임시 사용자 JSON이 반환됨
        when(redisCustomService.getRedisData(redisKey)).thenReturn(tempUserJson);
        // given: jwtUtil이 최종 JWT 토큰을 생성
        String expectedToken = "final.jwt.token";
        when(jwtUtil.createAccessToken(email, userId)).thenReturn(expectedToken);

        // given: 테스트용 DTO (익명 클래스로 구현)
        UserRequestDTO.UserUniversityAndMajorDTO dto = new UserRequestDTO.UserUniversityAndMajorDTO() {
            @Override
            public String getUniversityName() {
                return "Test University";
            }
            @Override
            public String getMajor() {
                return "Test Major";
            }
        };

        // when: completeSignup 메서드 호출
        String actualToken = completeSignupService.completeSignup(email, dto);

        // then: 최종 JWT 토큰이 올바르게 반환되어야 함
        assertEquals(expectedToken, actualToken);

        // then: userRepository.save()가 업데이트된 사용자 객체를 저장했는지 검증
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository, times(1)).save(userCaptor.capture());
        User savedUser = userCaptor.getValue();

        assertEquals("Test Major", savedUser.getMajor());
        assertEquals(email, savedUser.getEmail());
        assertEquals("Test User", savedUser.getName());

        // then: Redis의 임시 데이터 삭제가 호출되었는지 확인
        verify(redisCustomService, times(1)).deleteRedisData(redisKey);
    }

    */
/**
     * Redis에서 임시 사용자 정보가 조회되지 않을 경우, NOT_FOUND 에러가 발생하는지 테스트합니다.
     *//*

    @Test
    public void testCompleteSignup_TempUserNotFound() {
        // given: Redis에서 null 반환 (임시 데이터 없음)
        when(redisCustomService.getRedisData(redisKey)).thenReturn(null);

        // given: 테스트용 DTO (익명 클래스 구현)
        UserRequestDTO.UserUniversityAndMajorDTO dto = new UserRequestDTO.UserUniversityAndMajorDTO() {
            @Override
            public String getUniversityName() {
                return "Test University";
            }
            @Override
            public String getMajor() {
                return "Test Major";
            }
        };

        // when & then: completeSignup 호출 시 ResponseStatusException 발생 확인 (HTTP 404)
        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> completeSignupService.completeSignup(email, dto));
        assertEquals(HttpStatus.NOT_FOUND, ex.getStatusCode());
    }
}*/
