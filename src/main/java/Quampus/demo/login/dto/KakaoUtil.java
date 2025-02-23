package Quampus.demo.login.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

@Component
@Log4j2
public class KakaoUtil {

    @Value("${spring.security.oauth2.client.registration.kakao.client-id}")
    private String client;

    @Value("${spring.security.oauth2.client.registration.kakao.redirect-uri}")
    private String redirectUri;

    @Value("${spring.security.oauth2.client.registration.kakao.client-secret}")
    private String clientSecret;

    /**
     * 카카오 서버에 요청하여 엑세스 토큰을 가져오는 메서드
     * @param code 카카오에서 반환한 인가 코드
     * @return OAuthToken 객체(엑세스 토큰 포함)
     */
    public KakaoDTO.OAuthToken requestToken(String code) {
        // REST API 호출을 위한 RestTemplate 객체 생성
        RestTemplate restTemplate = new RestTemplate();

        // 요청 헤더 설정 (Content-Type을 x-www-form-urlencoded로 지정)
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // 요청 파라미터 설정 (카카오 OAuth2 토큰 요청 시 필요한 데이터)
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", client);
        params.add("client_secret", clientSecret);
        params.add("redirect_uri", redirectUri);
        params.add("code", code);

        // HTTP 요청 객체 생성 (헤더와 파라미터 포함)
        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest = new HttpEntity<>(params, headers);

        // 카카오 서버에 HTTP POST 요청을 보내서 액세스 토큰을 요청
        ResponseEntity<String> response = restTemplate.exchange(
                // 카카오 OAuth 토큰 발급 URL
                "https://kauth.kakao.com/oauth/token"
                , HttpMethod.POST
                , kakaoTokenRequest
                , String.class);

        // JSON 문자열을 객체로 변환하기 위한 ObjectMapper 생성
        ObjectMapper objectMapper = new ObjectMapper();

        // JSON 응답을 KakaoDTO.OAuthToken 객체로 변환
        KakaoDTO.OAuthToken oAuthToken = null;
        try {
            oAuthToken = objectMapper.readValue(response.getBody(), KakaoDTO.OAuthToken.class);
            // 액세스 토큰 로그 출력
            log.info("oAuthToken : " + oAuthToken.getAccess_token());
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to parse OAuth token", e);
        }

        // 발급된 OAuth 토큰 반환
        return oAuthToken;
    }

    /**
     * 카카오 API를 호출하여 사용자 프로필 정보를 가져오는 메서드
     * @param oAuthToken  카카오에서 발급받은 액세스 토큰
     * @return KakaoProfile 객체 (사용자 정보 포함)
     */
    public KakaoDTO.KakaoProfile requestProfile(KakaoDTO.OAuthToken oAuthToken){
        // REST API 호출을 위한 RestTemplate 객체 생성
        RestTemplate restTemplate2 = new RestTemplate();

        // 요청 헤더 설정 (Content-Type과 Authorization 포함)
        HttpHeaders headers2 = new HttpHeaders();
        headers2.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // 액세스 토큰을 Authorization 헤더에 추가
        headers2.add("Authorization","Bearer "+ oAuthToken.getAccess_token());

        // HTTP 요청 객체 생성 (헤더만 포함)
        HttpEntity<MultiValueMap<String,String>> kakaoProfileRequest = new HttpEntity <>(headers2);

        // 카카오 API를 호출하여 사용자 정보를 가져옴
        ResponseEntity<String> response2 = restTemplate2.exchange(
                // 카카오 사용자 정보 요청 URL
                "https://kapi.kakao.com/v2/user/me"
                , HttpMethod.GET
                , kakaoProfileRequest
                , String.class);

        // JSON 문자열을 객체로 변환하기 위한 ObjectMapper 생성
        ObjectMapper objectMapper = new ObjectMapper();

        // 카카오 프로필 정보를 저장할 객체
        KakaoDTO.KakaoProfile kakaoProfile = null;
        try {
            // JSON 응답을 KakaoDTO.KakaoProfile 객체로 변환
            kakaoProfile = objectMapper.readValue(response2.getBody(), KakaoDTO.KakaoProfile.class);
        } catch (JsonProcessingException e) {
            log.info(Arrays.toString(e.getStackTrace()));
            throw new RuntimeException("Failed to parse Kakao profile", e);
        }
        // 사용자 프로필 정보 반환
        return kakaoProfile;
    }
}
