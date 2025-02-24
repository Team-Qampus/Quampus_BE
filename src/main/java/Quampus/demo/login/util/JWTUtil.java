package Quampus.demo.login.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Base64;
import java.util.Date;

/**
 * JWT(JSON Web Token) 생성 및 검증을 담당하는 유틸리티 클래스
 */
@Component
@RequiredArgsConstructor
public class JWTUtil {

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private long validityInMilliseconds;


    // 의존성 주입이 끝난 후 실행되는 초기화 메서드
    // secretKey를 Base64 인코딩하여 보안 강화
    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }


    /**
     * JWT Access Token 생성 메서드
     * @param email 사용자 이메일 (토큰에 저장할 정보)
     * @return 생성된 JWT 토큰 (문자열)
     */
    public String createAccessToken(String email) {
        // 토큰에 담을 클레임(Claims) 설정 (이메일을 subject로 저장)
        Claims claims = Jwts.claims().setSubject(email);

        // 현재 시간과 만료 시간 설정
        Date now = new Date();
        Date validity = new Date(now.getTime() + validityInMilliseconds);

        // JWT 생성 및 반환
        return Jwts.builder()
                .setClaims(claims) // 사용자 정보(Claims) 설정
                .setIssuedAt(now) // 토큰 발급 시간
                .setExpiration(validity) // 토큰 만료 시간
                .signWith(SignatureAlgorithm.HS256, secretKey)  // HMAC SHA-256 알고리즘을 사용하여 서명
                .compact();  // 최종적으로 JWT 토큰을 문자열로 반환
    }


    /**
     * JWT에서 이메일(subject) 추출
     * @param token JWT 토큰
     * @return 추출된 이메일
     */
    public String getEmailFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(secretKey) // 서명 검증을 위해 secretKey 사용
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
                    .setSigningKey(secretKey) // 서명 검증을 위해 secretKey 사용
                    .parseClaimsJws(token) // 토큰을 파싱하여 클레임 추출
                    .getBody();

            // 현재 시간과 만료 시간 비교하여 유효성 체크
            return !claims.getExpiration().before(new Date());
        } catch (Exception e) {
            // 만료되었거나, 변조된 경우 false 반환
            return false;
        }
    }
}
