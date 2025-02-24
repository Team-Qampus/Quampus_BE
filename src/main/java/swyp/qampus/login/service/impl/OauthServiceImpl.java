package swyp.qampus.login.service.impl;

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
    public User oAuthLogin(String code, HttpServletResponse httpServletResponse) {

        // 카카오 서버에서 인가 코드를 사용하여 액세스 토큰 요청
        KakaoDTO.OAuthToken oAuthToken = kakaoUtil.requestToken(code);
        System.out.println("oAuthToken = " + oAuthToken);

        // 액세스 토큰을 이용하여 카카오 사용자 프로필 정보 요청
        KakaoDTO.KakaoProfile kakaoProfile = kakaoUtil.requestProfile(oAuthToken);
        System.out.println("kakaoProfile = " + kakaoProfile);

        // 카카오에서 받은 이메일 정보를 기반으로 기존 사용자 조회
        Optional<User> queryUser = userRepository.findByEmail(kakaoProfile.getKakao_account().getEmail());
        System.out.println("queryUser = " + queryUser);

        // 기존 회원이 존재하는 경우
        if (queryUser.isPresent()) {
            User user = queryUser.get();

            // JWT 액세스 토큰을 생성하여 응답 헤더에 추가
            httpServletResponse.setHeader("Authorization", jwtUtil.createAccessToken(user.getEmail()));
            return user; // 기존 사용자 반환
        } else {
            // 기존 회원이 존재하지 않는 경우, 새 사용자 생성
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


            // 임시 사용자 정보를 Redisdp JSON 형태로 저장
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                String userJson = objectMapper.writeValueAsString(tempUser);
                String key = "tempUser:" + tempUser.getEmail();

                // RedisCustomServiceImpl의 saveRedisData(key, value, limitTime)을 사용
                redisCustomService.saveRedisData(key, userJson, 1800L);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }

            // 임시 로드인 상태용 JWT 발급(추가 정보 입력 전까지 사용)
            String tempJwt = jwtUtil.createAccessToken(tempUser.getEmail());
            httpServletResponse.setHeader("Authorization",tempJwt);


            // DB에는 저장하지 않고 임시 사용자 정보를 반환
            return tempUser;
        }
    }


    @Override
    public void kakaoLogout(String accessToken) throws JsonProcessingException {
        // HTTP Header 생성
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        // HTTP 요청 보내기
        HttpEntity<MultiValueMap<String, String>> kakaoLogoutRequest = new HttpEntity<>(headers);
        String url = "https://kapi.kakao.com/v1/user/logout";

        ResponseEntity<String> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                kakaoLogoutRequest,
                String.class
        );

        // 응답 처리
        if (response.getStatusCode() == HttpStatus.OK) {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(response.getBody());

            Long id = jsonNode.get("id").asLong();
            log.info("카카오 로그아웃 완료 - 사용자 ID: {}", id);
        } else {
            log.error("카카오 로그아웃 실패 - 응답 코드: {}", response.getStatusCode());
            throw new RuntimeException("카카오 로그아웃 실패");
        }
    }
}
