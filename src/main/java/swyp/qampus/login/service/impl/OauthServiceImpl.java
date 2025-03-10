package swyp.qampus.login.service.impl;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.server.ResponseStatusException;
import swyp.qampus.login.config.data.RedisCustomServiceImpl;
import swyp.qampus.login.converter.OAuthConverter;
import swyp.qampus.login.dto.KakaoDTO;
import swyp.qampus.login.dto.KakaoUtil;
import swyp.qampus.login.entity.User;
import swyp.qampus.login.repository.UserRepository;
import swyp.qampus.login.service.OauthService;
import swyp.qampus.login.util.JWTUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;


@Service
@AllArgsConstructor
@Log4j2
public class OauthServiceImpl implements OauthService {

    // 카카오 API와 통신하는 유틸 클래스
    private final KakaoUtil kakaoUtil;
    // JWT 토큰을 생성 및 관리하는 유틸 클래스
    private final JWTUtil jwtUtil;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RestTemplate restTemplate;
    private final RedisCustomServiceImpl redisCustomService;

    /**
     * 카카오 OAuth 로그인을 수행하는 메서드
     * @param code 카카오에서 반환한 인가 코드
     * @param httpServletResponse JWT 토큰을 응답 헤더에 추가하기 위한 HttpServletResponse 객체
     * @return 로그인한 사용자 객체 (기존 회원 또는 신규 생성된 회원)
     */
    @Override
    public User oAuthLogin(String code, HttpServletResponse httpServletResponse, HttpServletRequest httpServletRequest) {
        log.info(" [oAuthLogin] 카카오 OAuth 로그인 시작");
        // 카카오 서버에서 인가 코드를 사용하여 액세스 토큰 요청
        KakaoDTO.OAuthToken oAuthToken = kakaoUtil.requestToken(code);
        log.info("[oAuthLogin] 받은 카카오 액세스 토큰: {}", oAuthToken);
        System.out.println("oAuthToken = " + oAuthToken);

        // 액세스 토큰을 이용하여 카카오 사용자 프로필 정보 요청
        KakaoDTO.KakaoProfile kakaoProfile = kakaoUtil.requestProfile(oAuthToken);
        System.out.println("kakaoProfile = " + kakaoProfile);
        log.info("[oAuthLogin] 받은 카카오 프로필 정보: {}", kakaoProfile);

        // 카카오에서 받은 이메일 정보를 기반으로 기존 사용자 조회
        Optional<User> queryUser = userRepository.findByEmail(kakaoProfile.getKakao_account().getEmail());
        System.out.println("queryUser = " + queryUser);
        log.info(" [oAuthLogin] 기존 사용자 조회 결과: {}", queryUser.isPresent() ? "존재함" : "없음");

        // 요청 헤더에서 기존 JWT 가져오기
        String existingToken = httpServletRequest.getHeader("Authorization");
        if (existingToken != null && existingToken.startsWith("Bearer ")) {
            existingToken = existingToken.substring(7); //  "Bearer " 제거하여 JWT만 남김
            log.info("[oAuthLogin] 클라이언트에서 보낸 기존 JWT: {}", existingToken);
        } else {
            log.info("[oAuthLogin] 기존 JWT 없음");
        }

        // 기존 회원이 존재하는 경우
        if (queryUser.isPresent()) {
            User user = queryUser.get();

            if (user.getUniversity() == null) {
                log.error("[oAuthLogin] university 정보가 조회되지 않음. (email: {})", user.getEmail());
            } else {
                log.info("[oAuthLogin] university 조회 성공: {}", user.getUniversity().getUniversityName());
            }

            // 기존 JWT가 유효한지 확인
            if (existingToken != null && jwtUtil.validateToken(existingToken)) {
                log.info("[oAuthLogin] 기존 JWT가 유효하므로 새로운 토큰을 발급하지 않음.");
                httpServletResponse.setHeader("Authorization", "Bearer " + existingToken);
                return user;
            }

            // 기존 JWT가 없거나 만료되었을 경우에만 새로운 JWT 발급
            log.info("[oAuthLogin] 기존 JWT가 없거나 만료됨. 새로운 JWT 발급");
            String newToken = jwtUtil.createAccessToken(user.getEmail(), user.getUserId());
            httpServletResponse.setHeader("Authorization", "Bearer " + newToken);
            log.info("[oAuthLogin] 새로 발급된 JWT: {}", newToken);
            return user;
        }
        

        // 기존 회원이 존재하지 않는 경우, 새 사용자 생성
        log.info("[oAuthLogin] 기존 회원 없음, 임시 사용자 생성");
        User tempUser = OAuthConverter.toUser(
                    // 이메일
                    kakaoProfile.getKakao_account().getEmail(),
                    // 이름
                    kakaoProfile.getKakao_account().getProfile().getNickname(),
                    // 닉네임
                    kakaoProfile.getKakao_account().getProfile().getNickname(),
                    // 임시 비밀번호
                    "1234",
                    // 비밀번호 인코더 적용
                    passwordEncoder,
                    // 이미지 URL
                    kakaoProfile.getKakao_account().getProfile().getProfileImageUrl()
                    );

        //  Redis에 기존 임시 사용자가 있는지 확인 후 저장
        String key = "tempUser:" + tempUser.getEmail();
        if (redisCustomService.getRedisData(key) == null) {
            try {
                String userJson = new ObjectMapper().writeValueAsString(tempUser);
                redisCustomService.saveRedisData(key, userJson, 1800L); // 30분 유지
                log.info("[oAuthLogin] 임시 사용자 정보를 Redis에 저장 (Key: {})", key);
            } catch (JsonProcessingException e) {
                log.error("[oAuthLogin] 임시 사용자 Redis 저장 중 오류 발생: {}", e.getMessage());
            }
        }

        // 기존 JWT가 유효하면 그대로 유지
        if (existingToken != null && jwtUtil.validateToken(existingToken)) {
            log.info("[oAuthLogin] 기존 JWT가 유효하므로 새로운 JWT를 발급하지 않음.");
            httpServletResponse.setHeader("Authorization", "Bearer " + existingToken);
            return tempUser;
        }

        // 기존 JWT가 없거나 만료되었을 경우에만 새로운 JWT 발급
        String tempJwt = jwtUtil.createAccessToken(tempUser.getEmail(), tempUser.getUserId());
        httpServletResponse.setHeader("Authorization", "Bearer " + tempJwt);
        log.info("[oAuthLogin] 새로 발급된 JWT: {}", tempJwt);
        return tempUser;

    }
}
