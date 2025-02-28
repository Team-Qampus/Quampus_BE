package swyp.qampus.login.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import swyp.qampus.exception.CommonErrorCode;
import swyp.qampus.exception.RestApiException;
import swyp.qampus.login.entity.User;
import swyp.qampus.login.repository.UserRepository;
import swyp.qampus.login.service.UserService;
import swyp.qampus.login.util.JWTUtil;
import swyp.qampus.question.domain.MyQuestionResponseDto;
import swyp.qampus.question.domain.Question;
import swyp.qampus.question.exception.QuestionErrorCode;
import swyp.qampus.question.repository.QuestionRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final QuestionRepository questionRepository;
    private final JWTUtil jwtUtil;

    @Override
    public List<MyQuestionResponseDto> getMyQuestions(String token, Long categoryId, String sort, Pageable pageable) {
        User user = userRepository.findById(jwtUtil.getUserIdFromToken(token))
                .orElseThrow(() -> new RestApiException(CommonErrorCode.USER_NOT_FOUND));

        List<Question> questions = questionRepository.findMyQuestions(user, categoryId, sort, pageable);

        if (questions.isEmpty()) {
            throw new RestApiException(QuestionErrorCode.NOT_EXIST_QUESTION);
        }

        return questions.stream()
                .map(MyQuestionResponseDto::of)
                .collect(Collectors.toList());
    }

    @Override
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
