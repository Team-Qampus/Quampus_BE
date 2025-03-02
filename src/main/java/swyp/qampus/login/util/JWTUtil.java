package swyp.qampus.login.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySources;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import swyp.qampus.login.entity.User;
import swyp.qampus.login.repository.UserRepository;
import swyp.qampus.university.domain.University;
import swyp.qampus.university.repository.UniversityRepository;

import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.Optional;
import java.util.function.Function;

/**
 * JWT(JSON Web Token) 생성 및 검증을 담당하는 유틸리티 클래스
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class JWTUtil {

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private long validityInMilliseconds;
    private  Key key;
    private static final String FREE_PASS_ROLE = "ROLE_TEST";
    private final UserRepository userRepository;
    private final UniversityRepository universityRepository;

    // 의존성 주입이 끝난 후 실행되는 초기화 메서드
    // secretKey를 Base64 인코딩하여 보안 강화
    @PostConstruct
    protected void init() {
        if(secretKey==null || secretKey.isBlank()){
            log.error("secretKey is NULL!!!!");
        }
        try {
            byte[] decodedKey = Decoders.BASE64URL.decode(secretKey); // URL-safe Base64 디코딩
            this.key= Keys.hmacShaKeyFor(decodedKey);

            if (decodedKey.length < 32) {
                throw new IllegalArgumentException("JWT Secret Key must be at least 256 bits (32 bytes) for HS256.");
            }

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
                .signWith(key ,SignatureAlgorithm.HS256) // secretKey를 바이트 배열로 변환
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
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        }catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e){
            log.info("Invalid JWT Token",e);
        }catch (ExpiredJwtException e){
            log.info("Expired JWT Token",e);
        }catch (UnsupportedJwtException e){
            log.info("Unsupported  JWT token",e);
        }catch (IllegalArgumentException e){
            log.info("JWT claims string is empty",e);
        }
        return false;
    }

    /**
     * JWT에서 유저 아이디(userId) 추출
     *
     * @param token
     * @return
     */
    public Long getUserIdFromToken(String token) {

        return parseClaims(token).get("userId", Long.class);
    }
    private Claims parseClaims(String token) {
        try {
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
        }catch (ExpiredJwtException e){
            return e.getClaims();
        }
    }
    private <T> T getClaimFromToken(final String token, final Function<Claims,T>claimsTFunction){
        if(Boolean.FALSE.equals(validateToken(token))){
            return null;
        }
        final Claims claims=parseClaims(token);

        return claimsTFunction.apply(claims);
    }
}
