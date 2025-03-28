package swyp.qampus.login.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import swyp.qampus.exception.CommonErrorCode;
import swyp.qampus.exception.RestApiException;
import swyp.qampus.login.dto.MyPageResponseDto;
import swyp.qampus.login.entity.User;
import swyp.qampus.login.repository.UserRepository;
import swyp.qampus.login.service.UserService;
import swyp.qampus.login.util.JWTUtil;
import swyp.qampus.openApi.GetLocationUtil;
import swyp.qampus.openApi.LocationDto;
import swyp.qampus.question.domain.MyQuestionResponseDto;
import swyp.qampus.question.domain.Question;
import swyp.qampus.question.repository.QuestionRepository;
import swyp.qampus.university.domain.University;
import swyp.qampus.university.repository.UniversityRepository;

import java.net.URISyntaxException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final QuestionRepository questionRepository;
    private final UniversityRepository universityRepository;
    private final JWTUtil jwtUtil;
    private final GetLocationUtil getLocationUtil;

    @Override
    public MyPageResponseDto getMyPageData(String token, Long categoryId, String sort, Pageable pageable) {
        User user = userRepository.findById(jwtUtil.getUserIdFromToken(token))
                .orElseThrow(() -> new RestApiException(CommonErrorCode.USER_NOT_FOUND));

        Page<Question> questionPage = questionRepository.findMyQuestions(user, categoryId, sort, pageable);

        Page<MyQuestionResponseDto> questionDtos = questionPage.map(MyQuestionResponseDto::of);

        return MyPageResponseDto.of(user, questionDtos);
    }


    @Transactional
    @Override
    public String testUser(String userName,String universityName,String major) throws URISyntaxException {

        //LocationDto locationDto = getLocationUtil.findLocationByCompanyName(universityName);
        University university = universityRepository.findByUniversityName(universityName)
                .orElseGet(() ->{
                    List<University> existingUniversities=universityRepository
                            .findAllByUniversityName(universityName);

                    if(!existingUniversities.isEmpty()){
                        return existingUniversities.get(0);
                    }

                    University newUniversity=University.builder()
                            .universityName(universityName)
                            .latitude(0.0)
                            .longitude(0.0)
                            .build();

                    return universityRepository.save(newUniversity);
                });

        User user=User.builder()
                .nickname("test")
                .email("email"+userName+"@naver.com")
                .name(userName)
                .password("12345@sa")
                .university(university)
                .major(major)
                .build();
        user = userRepository.save(user);
        return jwtUtil.createAccessToken("email" + userName + "@naver.com", user.getUserId());
    }


    @Transactional
    @Scheduled(cron = "1 0 0 1 * * ")
    public void userResetMonthly() {
        userRepository.resetMonthlyChoiceCnt();
        log.info("유저 monthlyChoice 초기화");
    }

    @Override
    @Transactional
    @Scheduled(cron = "59 59 23 * * 7")
    public void userResetWeekly() {
        userRepository.resetWeeklyChoiceCnt();
        log.info("유저 weeklyChoice 초기화");
    }
}
