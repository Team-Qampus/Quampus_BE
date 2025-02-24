package swyp.qampus.test;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import swyp.qampus.login.entity.User;
import swyp.qampus.login.repository.UserRepository;
import swyp.qampus.university.domain.University;
import swyp.qampus.university.repository.UniversityRepository;


@RestController
@RequiredArgsConstructor
@Tag(name = "회원가입 서버test용입니다. 사용X")
public class TestController {
    private final UserRepository userRepository;
    private final UniversityRepository universityRepository;

    @PostMapping("/signup")
    public ResponseEntity<?>signup(@RequestBody TestDto testDto){
        User user=User.builder()
                .name(testDto.getName())
                .email(testDto.getEmail())
                .major(testDto.getMajor())
                .nickname(testDto.getName())
                .password(testDto.getName())
                .build();

        University university = universityRepository.findByUniversityName(testDto.getUniversityName())
                .orElseGet(() -> {
                    University newUniversity = University.builder()
                            .universityName(testDto.getUniversityName())
                            .build();
                    return universityRepository.save(newUniversity);
                });
        user.setUniversity(university);
        userRepository.save(user);
        university.addUser(user);
        return ResponseEntity.ok().body("성공");
    }
}
