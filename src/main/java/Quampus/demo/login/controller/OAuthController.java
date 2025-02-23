package Quampus.demo.login.controller;

import Quampus.demo.login.converter.UserConverter;
import Quampus.demo.login.dto.UserResponseDTO;
import Quampus.demo.login.entity.User;
import Quampus.demo.login.service.OauthService;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class OAuthController {

    private final OauthService oauthService;

    /**
     * 카카오 로그인 요청을 처리하는 API 엔드포인트
     * @param code 카카오 로그인 후 받은 인가 코드
     * @param httpServletResponse JWT 토큰을 응답 헤더에 추가하기 위한 객체
     * @return UserResponseDTO.JoinResultDTO (로그인된 사용자 정보 반환)
     */
    @GetMapping("/auth/login/kakao")
    public ResponseEntity<UserResponseDTO.JoinResultDTO> kakaoLogin(@RequestParam("code") String code
            , HttpServletResponse httpServletResponse) {

        // 카카오 OAuth 로그인 처리
        User user = oauthService.oAuthLogin(code, httpServletResponse);

        // User 엔티티를 DTO로 변환하여 응답
        return ResponseEntity.ok(UserConverter.toJoinResultDTO(user));
    }

    /**
     * 카카오 로그아웃 API
     * @param accessToken 클라이언트가 제공한 카카오 액세스 토큰 (`Authorization` 헤더에서 가져옴)
     * @return 로그아웃 성공 또는 실패 응답 메시지
     */
    @PostMapping("/auth/logout/kakao")
    public ResponseEntity<String> logout(@RequestHeader("Authorization") String accessToken) {
        try {
            // 1. "Bearer " 접두어 제거 후, 실제 액세스 토큰만 추출
            oauthService.kakaoLogout(accessToken.replace("Bearer ", "")); // 토큰에서 "Bearer " 제거 후 전달

            // 2. 카카오 로그아웃 성공 시 응답 반환
            return ResponseEntity.ok("카카오 로그아웃 성공");
        } catch (JsonProcessingException e) {
            // 3. JSON 파싱 중 예외 발생 시, 500 에러 반환
            return ResponseEntity.status(500).body("카카오 로그아웃 실패: " + e.getMessage());
        }
    }
}
