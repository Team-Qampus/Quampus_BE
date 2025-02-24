//package swyp.qampus.login.util;
//
//import io.jsonwebtoken.*;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//
//import java.util.Base64;
//import java.util.Date;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.junit.jupiter.api.Assertions.*;
//
//class JWTUtilTest {
//
//    private JWTUtil jwtUtil;
//
//    // 테스트를 위한 고정된 secretKey 설정 (application.properties 값과 동일해야 함)
//    private final String secretKey = "fDEysqmgauEyvUkAa55TjDef3HK-NOtuQ1LwOc3eRrs=";
//
//    // 1시간 (1000 * 60 * 60)
//    private final long validityInMilliseconds = 3600000;
//
//    @BeforeEach
//    void setUp() {
//        jwtUtil = new JWTUtil();
//
//        // Base64 URL-safe 디코딩 적용
//        jwtUtil.secretKey = new String(Base64.getUrlDecoder().decode(secretKey));
//        jwtUtil.validityInMilliseconds = validityInMilliseconds;
//    }
//
//    @Test
//    void testCreateAccessToken_And_GetEmailFromToken() {
//        // Given
//        String email = "test@example.com";
//        Long userId = 1L;
//
//        // When
//        String token = jwtUtil.createAccessToken(email, userId);
//        String extractedEmail = jwtUtil.getEmailFromToken(token);
//
//        // Then
//        assertThat(extractedEmail).isEqualTo(email);
//    }
//
//    @Test
//    void testCreateAccessToken_And_ValidateToken() {
//        // Given
//        String email = "valid@example.com";
//        Long userId = 1L;
//        String token = jwtUtil.createAccessToken(email, userId);
//
//        // When
//        boolean isValid = jwtUtil.validateToken(token);
//
//        // Then
//        assertThat(isValid).isTrue();
//    }
//
//    @Test
//    void testValidateToken_ExpiredToken() throws InterruptedException {
//        // Given
//        String email = "expired@example.com";
//        Long userId = 1L;
//
//        // 유효 기간을 1초로 설정하여 만료된 토큰 생성
//        jwtUtil.validityInMilliseconds = 1000;
//        String token = jwtUtil.createAccessToken(email, userId);
//
//        // 2초 대기 후 검증 (토큰이 만료되었는지 확인)
//        Thread.sleep(2000);
//
//        // When
//        boolean isValid = jwtUtil.validateToken(token);
//
//        // Then
//        assertThat(isValid).isFalse();
//    }
//
//
//    @Test
//    void testCreateAccessToken_And_GetUserIdFromToken() {
//        // Given
//        String email = "user@example.com";
//        Long userId = 123L;
//
//        // JWT에 userId를 포함하여 생성
//        String token = Jwts.builder()
//                .setSubject(email)
//                .claim("userId", userId)
//                .setIssuedAt(new Date())
//                .setExpiration(new Date(System.currentTimeMillis() + validityInMilliseconds))
//                .signWith(SignatureAlgorithm.HS256, jwtUtil.secretKey.getBytes())
//                .compact();
//
//        // When
//        Long extractedUserId = jwtUtil.getUserIdFromToken(token);
//
//        // Then
//        assertThat(extractedUserId).isEqualTo(userId);
//    }
//}