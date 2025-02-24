package Quampus.demo.login.service;

import Quampus.demo.login.config.data.RedisCustomServiceImpl;
import Quampus.demo.login.dto.UserRequestDTO;
import Quampus.demo.login.entity.User;
import Quampus.demo.login.repository.UserRepository;
import Quampus.demo.login.util.JWTUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class CompleteSignupService {

    private final UserRepository userRepository;
    private final RedisCustomServiceImpl redisCustomService;
    private final JWTUtil jwtUtil;
    private final ObjectMapper objectMapper = new ObjectMapper();

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

        // 추가 정보 병합 (DTO의 필드를 User의 필드에 매핑)
        User updateUser = tempUser.toBuilder()
                .universityName(request.getUniversityName())
                .major(request.getMajor())
                .build();

        // 최종적으로 DB에 저장
        userRepository.save(updateUser);

        // Redis에 저장된 임시 데이터 삭제
        redisCustomService.deleteRedisData(key);

        // 최종 JWT 토큰 생성 후 반환
        return jwtUtil.createAccessToken(updateUser.getEmail());
    }
}
