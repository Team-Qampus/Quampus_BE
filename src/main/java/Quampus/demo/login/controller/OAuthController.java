package Quampus.demo.login.controller;

import Quampus.demo.login.converter.UserConverter;
import Quampus.demo.login.dto.UserRequestDTO;
import Quampus.demo.login.dto.UserResponseDTO;
import Quampus.demo.login.entity.User;
import Quampus.demo.login.service.CompleteSignupService;
import Quampus.demo.login.service.OauthService;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class OAuthController {

    private final OauthService oauthService;
    private final CompleteSignupService completeSignupService;

    /**
     * 카카오 로그인 요청을 처리하는 API 엔드포인트
     * @param code 카카오 로그인 후 받은 인가 코드
     * @param httpServletResponse JWT 토큰을 응답 헤더에 추가하기 위한 객체
     * @return UserResponseDTO.JoinResultDTO (로그인된 사용자 정보 반환)
     */
    @GetMapping("/login/kakao")
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
    @PostMapping("/logout/kakao")
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

    /**
     * 최종 회원가입을 위한 추가 정보 입력 API 엔드포인트
     * - 쿼리 파라미터로 이메일을 받고, 본문으로 UserRequestDTO.UserUniversityAndMajorDTO (학교, 학과 정보)를 전달받습니다.
     */
    @PostMapping("/signup/complete")
    public ResponseEntity<String> completeSignup(@RequestParam("email") String email,
                                                 @RequestBody UserRequestDTO.UserUniversityAndMajorDTO request,
                                                 HttpServletResponse response) {
        // 서비스 계층에 이메일과 DTO를 전달하여 최종 회원가입 진행
        String finalJwt = completeSignupService.completeSignup(email, request);
        response.setHeader("Authorization", finalJwt);
        return ResponseEntity.ok("회원가입이 완료되었습니다.");
    }
}
