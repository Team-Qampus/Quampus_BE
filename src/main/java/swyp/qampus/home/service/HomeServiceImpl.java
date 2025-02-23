package swyp.qampus.home.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import swyp.qampus.answer.domain.AnswerWeeklyResponseDto;
import swyp.qampus.answer.repository.AnswerRepository;
import swyp.qampus.home.dto.HomeResponseDto;
import swyp.qampus.question.domain.QuestionWeeklyResponseDto;
import swyp.qampus.question.repository.QuestionRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HomeServiceImpl implements HomeService {
    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;
    private HomeResponseDto cachedResponse;

    @Override
    // 매주 월요일 00:00 (자정)에 데이터 갱신
    @Scheduled(cron = "0 0 0 * * MON")
    @Transactional
    public void updateWeeklyPopularContent() {
        List<QuestionWeeklyResponseDto> popularQuestions = questionRepository.findWeeklyPopularQuestions()
                .stream().map(QuestionWeeklyResponseDto::new).collect(Collectors.toList());

        List<AnswerWeeklyResponseDto> popularAnswers = answerRepository.findWeeklyPopularAnswers()
                .stream().map(AnswerWeeklyResponseDto::new).collect(Collectors.toList());

        cachedResponse = new HomeResponseDto(popularQuestions, popularAnswers);
    }

    public HomeResponseDto getWeeklyPopularContent() {
        if (cachedResponse == null) {
            updateWeeklyPopularContent();
        }
        return cachedResponse;
    }
}