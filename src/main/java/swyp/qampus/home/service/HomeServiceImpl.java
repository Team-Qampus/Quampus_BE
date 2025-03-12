package swyp.qampus.home.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import swyp.qampus.answer.domain.AnswerWeeklyResponseDto;
import swyp.qampus.answer.repository.AnswerRepository;
import swyp.qampus.exception.CommonErrorCode;
import swyp.qampus.exception.RestApiException;
import swyp.qampus.home.dto.HomeResponseDto;
import swyp.qampus.home.dto.UserHomeDto;
import swyp.qampus.home.exception.HomeErrorCode;
import swyp.qampus.login.entity.User;
import swyp.qampus.login.repository.UserRepository;
import swyp.qampus.login.util.JWTUtil;
import swyp.qampus.question.domain.QuestionWeeklyResponseDto;
import swyp.qampus.question.repository.QuestionRepository;
import swyp.qampus.university.domain.University;
import swyp.qampus.university.repository.UniversityRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HomeServiceImpl implements HomeService {
    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;
    private final UserRepository userRepository;
    private final UniversityRepository universityRepository;
    private final JWTUtil jwtUtil;
    private HomeResponseDto userCachedResponse;
    private HomeResponseDto guestCachedResponse;

    @Override
    // 매주 월요일 00:00 (자정)에 데이터 갱신
    @Scheduled(cron = "0 0 0 * * MON")
    @Transactional
    public void updateWeeklyPopularQna() {
        List<QuestionWeeklyResponseDto> popularQuestions = questionRepository.findWeeklyPopularQuestions()
                .stream().map(QuestionWeeklyResponseDto::of).collect(Collectors.toList());

        List<AnswerWeeklyResponseDto> popularAnswers = answerRepository.findWeeklyPopularAnswers()
                .stream().map(AnswerWeeklyResponseDto::of).collect(Collectors.toList());


        userCachedResponse = HomeResponseDto.withoutUser(popularQuestions, popularAnswers);
        guestCachedResponse = HomeResponseDto.fromQuestions(popularQuestions);
    }

    @Override
    public HomeResponseDto getUserHomeContent(String token) {
        User user = userRepository.findById(jwtUtil.getUserIdFromToken(token))
                .orElseThrow(() -> new RestApiException(CommonErrorCode.USER_NOT_FOUND));

        //유저 학교명
        String universityName=user.getUniversity().getUniversityName();
        //유저 학과명
        String userMajor=user.getMajor();
        //이번 달 학과 랭킹
        int thisMonthMajorRanking= userRepository.getThisMonthRankOfMajor(userMajor);
        //저번 달 학과 랭킹
        int lastMonthMajorRanking=userRepository.getLastMonthRankOfMajor(userMajor);
        //이번 달 학교 랭킹
        int thisMonthUniversityRanking= universityRepository.getThisMonthRankOfSchool(universityName);
        //저번 달 학교 랭킹
        int lastMonthUniversityRanking= universityRepository.getLastMonthRankOfSchool(universityName);

//        if (userCachedResponse == null) {
//            updateWeeklyPopularQna();
//        }

        updateWeeklyPopularQna();   // 더미데이터 즉시 반영

        UserHomeDto userHomeDto=UserHomeDto
                .builder()
                .major(user.getMajor())
                .name(user.getName())
                .university_name(universityName)
                .this_month_ranking(thisMonthUniversityRanking)
                .last_month_ranking(lastMonthUniversityRanking)
                .this_month_major_ranking(thisMonthMajorRanking)
                .last_month_major_ranking(lastMonthMajorRanking)
                .build();

        return HomeResponseDto.of(userHomeDto,
                userCachedResponse.getWeeklyQuestions(),
                userCachedResponse.getWeeklyAnswers());
    }

    @Override
    public HomeResponseDto getGuestHomeContent() {
//        if (guestCachedResponse == null) {
//            updateWeeklyPopularQna();
//        }
        updateWeeklyPopularQna();   //더미데이터 즉시 반영
        return guestCachedResponse;
    }
}
