package swyp.qampus.test;

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
