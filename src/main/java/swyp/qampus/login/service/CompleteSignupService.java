package swyp.qampus.login.service;

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
import swyp.qampus.university.domain.University;
import swyp.qampus.university.repository.UniversityRepository;

@Service
@RequiredArgsConstructor
public class CompleteSignupService {

    private final UserRepository userRepository;
    private final RedisCustomServiceImpl redisCustomService;
    private final JWTUtil jwtUtil;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final UniversityRepository universityRepository;

    public String completeSignup(String email, UserRequestDTO.UserUniversityAndMajorDTO request) {
        String key = "tempUser:" + email;
        String userJson = redisCustomService.getRedisData(key);

        if(userJson == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "임시로 저장된 사용자 정보가 존재하지 않거나 만료되었습니다.");
        }

        User tempUser;

        try {
            tempUser = objectMapper.readValue(userJson, User.class);
        } catch (JsonProcessingException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "임시로 저장된 사용자 정보를 파싱하는 중 오류가 발생되었습니다.");
        }

        // 1. universityName을 이용하여 University 조회
        University university = universityRepository.findByUniversityName(request.getUniversityName())
                .orElseGet(() -> universityRepository.save(University.builder()
                        .universityName(request.getUniversityName())
                        .build()));

        // 2. User 업데이트 (University 객체 설정)
        User updateUser = tempUser.toBuilder()
                .major(request.getMajor())
                .university(university)  // University 엔티티 연결
                .build();

        // 최종적으로 DB에 저장
        userRepository.save(updateUser);

        // Redis에 저장된 임시 데이터 삭제
        redisCustomService.deleteRedisData(key);

        // 최종 JWT 토큰 생성 후 반환
        return jwtUtil.createAccessToken(updateUser.getEmail(), updateUser.getUserId());
    }
}
