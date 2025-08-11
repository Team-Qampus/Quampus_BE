package swyp.qampus.login.service;

import lombok.extern.log4j.Log4j2;
import swyp.qampus.login.config.data.RedisCustomServiceImpl;
import swyp.qampus.login.dto.UserRequestDTO;
import swyp.qampus.login.entity.User;
import swyp.qampus.login.repository.UserRepository;
import swyp.qampus.login.util.JWTUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import swyp.qampus.openApi.GetLocationUtil;
import swyp.qampus.openApi.LocationDto;
import swyp.qampus.university.domain.University;
import swyp.qampus.university.repository.UniversityRepository;

import java.net.URISyntaxException;

@Service
@RequiredArgsConstructor
@Log4j2
public class CompleteSignupService {

    private final UserRepository userRepository;
    private final UniversityRepository universityRepository;
    private final RedisCustomServiceImpl redisCustomService;
    private final JWTUtil jwtUtil;
    private final GetLocationUtil getLocationUtil;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public String completeSignup(String email, UserRequestDTO.UserUniversityAndMajorDTO request, String existingToken) throws URISyntaxException {
        log.info("[completeSignup] 회원가입 완료 프로세스 시작 (email: {})", email);
        String key = "tempUser:" + email;
        String userJson = redisCustomService.getRedisData(key);

        if (userJson == null) {
            log.warn("[completeSignup] Redis에서 임시 사용자 정보가 존재하지 않음 (key: {})", key);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "임시로 저장된 사용자 정보가 존재하지 않거나 만료되었습니다.");
        }

        User tempUser;

        try {
            tempUser = objectMapper.readValue(userJson, User.class);
            log.info("[completeSignup] Redis에서 임시 사용자 정보 가져옴");
        } catch (JsonProcessingException e) {
            log.error("[completeSignup] 임시 사용자 정보를 파싱하는 중 오류 발생: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "임시로 저장된 사용자 정보를 파싱하는 중 오류가 발생되었습니다.");
        }

        String universityName = request.getUniversityName();
        LocationDto locationDto = getLocationUtil.findLocationByCompanyName(universityName);

        //user가 대학교 까지 입력한 경우
        if(universityName.contains("학교")){
            universityName=universityName.replace("학교","");
        }

        // 1. universityName을 이용하여 University 조회
        University university = universityRepository.findByUniversityName(universityName)
                .orElseGet(() -> universityRepository.save(University
                        .builder()
                        .universityName(request.getUniversityName().replace("대학교",""))
                        .latitude(Double.valueOf(locationDto.get위도()))
                        .longitude(Double.valueOf(locationDto.get경도()))
                        .build()));

        // 2. User 업데이트 (University 객체 설정)
        User updateUser = tempUser.toBuilder()
                .major(request.getMajor())
                .university(university)  // University 엔티티 연결
                .build();

        // 최종적으로 DB에 저장
        updateUser = userRepository.save(updateUser);
        log.info("[completeSignup] User 정보 업데이트 완료 (userId: {})", updateUser.getUserId());

        // Redis에 저장된 임시 데이터 삭제
        redisCustomService.deleteRedisData(key);
        log.info("[completeSignup] Redis에서 임시 사용자 정보 삭제 (key: {})", key);

        //  기존 JWT가 유효하면 새로운 JWT 발급하지 않고 그대로 반환
        if (jwtUtil.validateToken(existingToken)) {
            log.info("[completeSignup] 기존 JWT가 유효하므로 새로운 JWT를 발급하지 않음.");
            return existingToken;
        }

        // 기존 JWT가 없거나 만료되었을 경우에만 새로운 JWT 발급
        log.info("[completeSignup] 기존 JWT가 없거나 만료됨. 새로운 JWT 발급");
        String newJwt = jwtUtil.createAccessToken(updateUser.getEmail(), updateUser.getUserId());
        log.info("[completeSignup] 새로 발급된 JWT: {}", newJwt);
        return newJwt;
    }
}
