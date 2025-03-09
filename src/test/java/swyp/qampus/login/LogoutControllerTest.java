/*package swyp.qampus.login;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import swyp.qampus.login.controller.OAuthController;
import swyp.qampus.login.util.JWTUtil;


import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
public class LogoutControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JWTUtil jwtUtil;

    @Test
    @WithMockUser
    public void 로그아웃_성공_테스트() throws Exception {
        // ✅ 올바른 JWT 형식 사용 (header.payload.signature)
        String validToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJ1c2VySWQiLCJpYXQiOjE2MjAwMDAwMDB9.abc123";

        Claims claims = Jwts.claims().setSubject("test@example.com");
        claims.put("userId", 123L);

        // ✅ Mock 설정
        when(jwtUtil.resolveToken(any(HttpServletRequest.class))).thenReturn(validToken);
        when(jwtUtil.getClaims(validToken)).thenReturn(claims);
        when(jwtUtil.createExpiredToken(anyString(), anyLong())).thenReturn("expired.jwt.token");

        mockMvc.perform(post("/auth/logout")
                        .header("Authorization", "Bearer " + validToken))
                .andExpect(status().isOk())
                .andExpect(content().string("expired.jwt.token"));
    }

    @Test
    @WithMockUser
    public void 유효하지_않은_토큰_테스트() throws Exception {
        // ✅ 올바른 형식이지만 잘못된 서명 포함
        String invalidToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJ1c2VySWQiLCJpYXQiOjE2MjAwMDAwMDB9.wrong_signature";

        // ✅ `MalformedJwtException`을 던지도록 설정
        when(jwtUtil.resolveToken(any(HttpServletRequest.class))).thenReturn(invalidToken);
        when(jwtUtil.getClaims(invalidToken)).thenThrow(new MalformedJwtException("잘못된 JWT 토큰"));

        mockMvc.perform(post("/auth/logout")
                        .header("Authorization", "Bearer " + invalidToken))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("유효하지 않은 토큰"));
    }
}
 */