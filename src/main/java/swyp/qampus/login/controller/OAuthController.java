package swyp.qampus.login.controller;

import com.amazonaws.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.server.ResponseStatusException;
import swyp.qampus.common.ResponseDto;
import swyp.qampus.exception.ErrorCode;
import swyp.qampus.login.converter.UserConverter;
import swyp.qampus.login.dto.UserRequestDTO;
import swyp.qampus.login.dto.UserResponseDTO;
import swyp.qampus.login.entity.User;
import swyp.qampus.login.service.CompleteSignupService;
import swyp.qampus.login.service.OauthService;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import swyp.qampus.login.util.JWTUtil;
import swyp.qampus.question.domain.MyQuestionResponseDto;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
@Tag(name = "로그인",description = "로그인 API")
public class OAuthController {

    private final OauthService oauthService;
    private final CompleteSignupService completeSignupService;
    private final JWTUtil jwtUtil;

    /**
     * 카카오 로그인 요청을 처리하는 API 엔드포인트
     * @param code 카카오 로그인 후 받은 인가 코드
     * @param httpServletResponse JWT 토큰을 응답 헤더에 추가하기 위한 객체
     * @return UserResponseDTO.JoinResultDTO (로그인된 사용자 정보 반환)
     */
    @Operation(
            summary = "카카오 로그인 API입니다.-[담당자 : 홍기문]",
            responses = {
                    @ApiResponse(responseCode = "200", description = "성공",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = UserResponseDTO.JoinResultDTO.class))),
            }
    )
    @GetMapping("/login/kakao")
    public ResponseEntity<UserResponseDTO.JoinResultDTO> kakaoLogin(
            @Parameter(description = "카카오 로그인 후 받은 인가 코드")
            @RequestParam("code") String code,

            HttpServletResponse httpServletResponse
    ) {
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
    @Operation(
            summary = "카카오  로그아웃 API입니다.-[담당자 : 홍기문]",
            responses = {
                    @ApiResponse(responseCode = "200", description = "카카오 로그아웃 성공",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ResponseDto.class))),
                    @ApiResponse(responseCode = "500", description = "카카오 로그아웃 실패: ",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ResponseEntity.class))),
            }
    )
    @PostMapping("/logout/kakao")
    public ResponseEntity<?> logout(
            @Parameter(description = "클라이언트가 제공한 카카오 액세스 토큰 (`Authorization` 헤더에서 가져옴)")
            @RequestHeader("Authorization") String accessToken
    ) {
        try {
            // 1. "Bearer " 접두어 제거 후, 실제 액세스 토큰만 추출
            oauthService.kakaoLogout(accessToken.replace("Bearer ", "")); // 토큰에서 "Bearer " 제거 후 전달

            // 2. 카카오 로그아웃 성공 시 응답 반환
            return ResponseEntity.ok(ResponseDto.of(true,200,"카카오 로그아웃 성공"));
        } catch (JsonProcessingException e) {
            // 3. JSON 파싱 중 예외 발생 시, 500 에러 반환
            return ResponseEntity.status(500).body("카카오 로그아웃 실패: " + e.getMessage());
        }
    }

    /**
     * 최종 회원가입을 위한 추가 정보 입력 API 엔드포인트
     * - 쿼리 파라미터로 이메일을 받고, 본문으로 UserRequestDTO.UserUniversityAndMajorDTO (학교, 학과 정보)를 전달받습니다.
     */
    @Operation(
            summary = "회원가입 API입니다.-[담당자 : 홍기문]",
            responses = {
                    @ApiResponse(responseCode = "200", description = "회원가입이 완료되었습니다.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ResponseDto.class))),
                    @ApiResponse(responseCode = "404", description = "임시로 저장된 사용자 정보가 존재하지 않거나 만료되었습니다.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ResponseStatusException.class))),
                    @ApiResponse(responseCode = "500", description = "임시로 저장된 사용자 정보를 파싱하는 중 오류가 발생되었습니다.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ResponseStatusException.class))),
            }
    )
    @PostMapping("/signup/complete")
    public ResponseEntity<?> completeSignup(@RequestHeader("Authorization") String token,
                                            @RequestBody UserRequestDTO.UserUniversityAndMajorDTO request,
                                            HttpServletResponse response) {
        // 1. JWT에서 이메일 추출
        String email = jwtUtil.getEmailFromToken(token.replace("Bearer ", ""));

        // 2. 서비스 계층 호출해서 회원가입 완료 처리

        String finalJwt = completeSignupService.completeSignup(email, request);

        // 3. 새 JWT를 헤더에 추가한다.
        response.setHeader("Authorization", finalJwt);

        return ResponseEntity.ok(ResponseDto.of(true,200,"회원가입이 완료되었습니다."));
    }
}
