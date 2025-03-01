package swyp.qampus.login.util;

import lombok.extern.slf4j.Slf4j;
import swyp.qampus.login.entity.User;
import swyp.qampus.login.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

/**
 * JWT 인증 필터
 * - 모든 요청에서 JWT 토큰을 확인하고, 유효한 경우 사용자 인증 처리
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class JWTFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;
    private final UserRepository userRepository;

    /**
     * HTTP 요청이 들어올 때마다 실행되는 필터 메서드
     * - JWT를 검사하고, 인증된 사용자 정보를 `SecurityContextHolder`에 저장
     * @param request HTTP 요청 객체
     * @param response HTTP 응답 객체
     * @param filterChain 필터 체인 객체
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        // 요청 헤더에서 Authorization 헤더 추출
        String authorizationHeader = request.getHeader("Authorization");

        String email = null;
        String token = null;

        log.info("TOKEN!!!!!!"+authorizationHeader);
        // Bearer 토큰인지 확인 후 추출
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            token = authorizationHeader.substring(7); // "Bearer " 이후의 문자열이 실제 JWT 토큰
            email = jwtUtil.getEmailFromToken(token); // 토큰에서 이메일 정보 추출
        }

        // 이메일이 존재하고 현재 SecurityContextHolder에 인증 정보가 없는 경우
        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            // 이메일을 기반으로 데이터베이스에서 사용자 정보 조회
            Optional<User> userDetails = userRepository.findByEmail(email);

            // 사용자가 존재하고, JWT 토큰이 유효한 경우
            if (userDetails.isPresent() && jwtUtil.validateToken(token)) {

                // Spring Security의 인증 객체 생성 (현재는 권한 정보를 사용하지 않음)
                UsernamePasswordAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken(userDetails.get(), null, null);

                // 요청 정보를 Authentication 객체에 추가
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // SecurityContextHolder에 인증 정보 저장 (이후 요청에서 인증된 사용자로 인식됨)
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        }

        // 다음 필터로 요청 전달 (JWT 검증 후에도 정상적인 요청 흐름 유지)
        filterChain.doFilter(request, response);
    }
}
