package swyp.qampus.test;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import swyp.qampus.login.entity.User;
import swyp.qampus.login.repository.UserRepository;
import swyp.qampus.login.util.JWTUtil;
import swyp.qampus.openApi.GetLocationUtil;
import swyp.qampus.university.domain.University;
import swyp.qampus.university.repository.UniversityRepository;

import java.net.URISyntaxException;


@RestController
@RequiredArgsConstructor
@Tag(name = "회원가입 서버test용입니다. 사용X")
public class TestController {
    private final UserRepository userRepository;
    private final UniversityRepository universityRepository;
    private final GetLocationUtil getLocationUtil;
    private final JWTUtil jwtUtil;
    @PostMapping("/signup")
    public ResponseEntity<?>signup(@RequestBody TestDto testDto){
        User user=User.builder()
                .name(testDto.getName())
                .email(testDto.getEmail())
                .major(testDto.getMajor())
                .nickname(testDto.getName())
                .password(testDto.getName())
                .build();

        //user가 대학교 까지 입력한 경우
        String universityName= testDto.getUniversityName();
        if(universityName.contains("학교")){
            universityName=universityName.replace("학교","");
        }
        University university = universityRepository.findByUniversityName(universityName)
                .orElseGet(() -> {
                    University newUniversity = University.builder()
                            .universityName(testDto.getUniversityName())
                            .build();
                    return universityRepository.save(newUniversity);
                });
        user.setUniversity(university);
        userRepository.save(user);
        university.addUser(user);
        return ResponseEntity.ok().body(jwtUtil.createAccessToken(user.getEmail(),user.getUserId()));
    }

    @GetMapping("/location")
    public ResponseEntity<?>location(@RequestParam("name")String name) throws URISyntaxException {
        return ResponseEntity.ok(getLocationUtil.findLocationByCompanyName(name));
    }
}
