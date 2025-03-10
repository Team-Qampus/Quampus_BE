
package swyp.qampus.login.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.jsonwebtoken.Claims;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import swyp.qampus.common.ResponseDto;
import swyp.qampus.login.converter.UserConverter;
import swyp.qampus.login.dto.UserRequestDTO;
import swyp.qampus.login.dto.UserResponseDTO;
import swyp.qampus.login.entity.User;
import swyp.qampus.login.service.CompleteSignupService;
import swyp.qampus.login.service.OauthService;
import swyp.qampus.login.util.JWTUtil;
import java.net.URISyntaxException;

@Log4j2
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
            HttpServletResponse httpServletResponse,
            HttpServletRequest httpServletRequest
    ) throws JsonProcessingException {
        // 카카오 OAuth 로그인 처리
        User user = oauthService.oAuthLogin(code, httpServletResponse, httpServletRequest);

        // 클라이언트가 JWT를 받을 수 있도록 로그 출력 (디버깅 목적)
        System.out.println("JWT 토큰: " + httpServletResponse.getHeader("Authorization"));

        // User 엔티티를 DTO로 변환하여 응답
        return ResponseEntity.ok(UserConverter.toJoinResultDTO(user));
    }

    /**
     * 로그아웃 API
     * @return 로그아웃 성공 또는 실패 응답 메시지
     */
    @Operation(
            summary = "로그아웃 API입니다.-[담당자 : 홍기문]",
            description = "클라이언트가 제공한 JWT 토큰을 기반으로 카카오 로그아웃을 수행합니다. " +
                    "토큰을 만료 처리하여 로그아웃을 완료합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "로그아웃 성공",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ResponseDto.class))),
                    @ApiResponse(responseCode = "500", description = "로그아웃 실패: ",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ResponseEntity.class))),
            }
    )
    @SecurityRequirement(name = "Bearer Authentication")
    @PostMapping("/logout")
    public ResponseEntity<?> logout(@Parameter(hidden = true) HttpServletRequest request) {

        String token = jwtUtil.resolveToken(request);
        if (token == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("토큰이 없습니다.");
        }

        try {
            Claims claims = jwtUtil.getClaims(token);
            String expiredToken = jwtUtil.createExpiredToken(claims.getSubject(), claims.get("userId", Long.class));
            return ResponseEntity.ok().body(expiredToken); // 만료된 토큰 반환
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("유효하지 않은 토큰");
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
                                            HttpServletResponse response) throws URISyntaxException {
        // 1. JWT에서 이메일 추출
        String email = jwtUtil.getEmailFromToken(token);

        // 2. 서비스 계층 호출해서 회원가입 완료 처리

        String finalJwt = completeSignupService.completeSignup(email, request, token);

        // 3. 새 JWT를 헤더에 추가한다.
        response.setHeader("Authorization", finalJwt);

        return ResponseEntity.ok(ResponseDto.of(true,200,"회원가입이 완료되었습니다."));
    }
}
