package swyp.qampus.login.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySources;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import swyp.qampus.login.entity.User;
import swyp.qampus.login.repository.UserRepository;
import swyp.qampus.university.domain.University;
import swyp.qampus.university.repository.UniversityRepository;

import java.util.Base64;
import java.util.Date;
import java.util.Optional;

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
     * 테스트용 프리패스 토큰 발급 메서드
     * @return 프리패스 토큰
     */
    public String createFreePassToken() {
        Claims claims = Jwts.claims().setSubject("testUser");
        claims.put("userId", 99999L);
        claims.put("role", FREE_PASS_ROLE);

        saveTestUserIfNotExists();

        Date now = new Date();
        Date validity = new Date(now.getTime() + validityInMilliseconds);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256, secretKey.getBytes())
                .compact();
    }

    private void saveTestUserIfNotExists() {
        Optional<User> existingUser = userRepository.findByEmail("testuser@example.com");
        if (existingUser.isEmpty()) {
            University testUniversity = universityRepository.findByUniversityName("테스트 대학교")
                    .orElseGet(() -> {
                        University newUniversity = University.builder()
                                .universityName("테스트 대학교")
                                .build();
                        return universityRepository.save(newUniversity);
                    });

            User testUser = User.builder()
                    .name("테스트 유저")
                    .email("testuser@example.com") // email로 찾기
                    .password("testpassword")
                    .major("테스트 학과")
                    .nickname("테스터")
                    .university(testUniversity)
                    .build();

            userRepository.save(testUser);
        }
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
}
