package swyp.qampus.login.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import swyp.qampus.login.repository.UserRepository;
import swyp.qampus.university.repository.UniversityRepository;

import java.util.Base64;
import java.util.Date;

/**
 * JWT(JSON Web Token) 생성 및 검증을 담당하는 유틸리티 클래스
 */
@Log4j2
@Component
@RequiredArgsConstructor
public class JWTUtil {

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private long validityInMilliseconds;

    private static final String FREE_PASS_ROLE = "ROLE_TEST";
    private final UserRepository userRepository;
    private final UniversityRepository universityRepository;

    // 의존성 주입이 끝난 후 실행되는 초기화 메서드
    // secretKey를 Base64 인코딩하여 보안 강화
    @PostConstruct
    protected void init() {
        try {
            byte[] decodedKey = Base64.getUrlDecoder().decode(secretKey); // URL-safe Base64 디코딩

            if (decodedKey.length < 32) {
                throw new IllegalArgumentException("JWT Secret Key must be at least 256 bits (32 bytes) for HS256.");
            }

            secretKey = new String(decodedKey);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid Base64 encoding for JWT Secret Key. Please check application.properties.", e);
        }
    }


    /**
     * JWT Access Token 생성 메서드
     * @param email 사용자 이메일 (토큰에 저장할 정보)
     * @return 생성된 JWT 토큰 (문자열)
     */
    public String createAccessToken(String email, Long userId) {
        // 토큰에 담을 클레임(Claims) 설정 (이메일을 subject로 저장)
        Claims claims = Jwts.claims().setSubject(email);
        claims.put("userId", userId);

        // 현재 시간과 만료 시간 설정
        Date now = new Date();
        Date validity = new Date(now.getTime() + validityInMilliseconds);

        // JWT 생성 및 반환
        return Jwts.builder()
                .setClaims(claims) // 사용자 정보(Claims) 설정
                .setIssuedAt(now) // 토큰 발급 시간
                .setExpiration(validity) // 토큰 만료 시간
                .signWith(SignatureAlgorithm.HS256, secretKey.getBytes()) // secretKey를 바이트 배열로 변환
                .compact();  // 최종적으로 JWT 토큰을 문자열로 반환
    }

    /**
     * JWT에서 이메일(subject) 추출
     * @param token JWT 토큰
     * @return 추출된 이메일
     */
    public String getEmailFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(secretKey.getBytes())// 서명 검증을 위해 secretKey 사용
                .parseClaimsJws(token) // 토큰을 파싱하여 클레임 추출
                .getBody()
                .getSubject(); // subject (이메일) 반환
    }


    /**
     * JWT 유효성 검증 메서드
     * @param token JWT 토큰
     * @return 유효한 토큰이면 true, 만료되었거나 변조된 토큰이면 false
     */
    public boolean validateToken(String token) {
        try {
            // JWT 토큰을 파싱하여 클레임 추출
            Claims claims = Jwts.parser()
                    .setSigningKey(secretKey.getBytes()) // 서명 검증을 위해 secretKey 사용
                    .parseClaimsJws(token) // 토큰을 파싱하여 클레임 추출
                    .getBody();

            if (FREE_PASS_ROLE.equals(claims.get("role"))) {
                return true;
            }

            // 현재 시간과 만료 시간 비교하여 유효성 체크
            return !claims.getExpiration().before(new Date());
        } catch (Exception e) {
            // 만료되었거나, 변조된 경우 false 반환
            return false;
        }
    }

    /**
     * JWT에서 유저 아이디(userId) 추출
     *
     * @param token
     * @return
     */
    public Long getUserIdFromToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(secretKey.getBytes())
                .parseClaimsJws(token)
                .getBody();

        return claims.get("userId", Long.class);
    }

    /**
     * JWT를 즉시 만료
     * @param email
     * @param userId
     * @return
     */
    public String createExpiredToken(String email, Long userId) {
        Claims claims = Jwts.claims().setSubject(email);
        claims.put("userId", userId);

        Date now = new Date();
        Date expiredTime = new Date(now.getTime()); // 즉시 만료

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expiredTime) // 만료 시간을 현재 시간으로 설정
                .signWith(SignatureAlgorithm.HS256, secretKey.getBytes())
                .compact();
    }

    // 헤더에서 JWT 가져오기
    public String resolveToken(HttpServletRequest request) {
        try {
            String token = request.getHeader("Authorization");

            if (token == null || token.trim().isEmpty()) {
                throw new IllegalArgumentException("Authorization 헤더가 존재하지 않거나 비어 있습니다.");
            }

            token = token.trim(); // 공백 제거

            // "Bearer "로 시작하면 "Bearer " 제거
            if (token.toLowerCase().startsWith("bearer ")) { // 대소문자 구분 없이
                token = token.substring(7).trim(); // "Bearer " 이후 부분
            }

            if (token.isEmpty()) {
                throw new IllegalArgumentException("JWT 토큰이 비어 있습니다.");
            }

            return token;

        } catch (IllegalArgumentException e) {
            log.error("JWT 토큰 추출 오류: {}", e.getMessage());
            return null;
        } catch (Exception e) {
            log.error("예상치 못한 오류로 인해 JWT 추출 실패: {}", e.getMessage());
            return null;
        }
    }

    // Claims 객체에서 데이터 추출
    public Claims getClaims(String token) {
        try {
            if (token == null || token.trim().isEmpty()) {
                throw new IllegalArgumentException("토큰이 존재하지 않거나 비어 있습니다.");
            }
            return Jwts.parser()
                    .setSigningKey(secretKey.getBytes()) // 서명 검증을 위해 secretKey 사용
                    .parseClaimsJws(token) // 토큰을 파싱하여 Claims 추출
                    .getBody();
        } catch (Exception e) {
            throw new RuntimeException("토큰이 유효하지 않거나 변조되었습니다.");
        }
    }
}
