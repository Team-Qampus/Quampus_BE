package swyp.qampus.question.repository;

import org.springframework.data.domain.Pageable;
import swyp.qampus.ai.domain.Ai;
import swyp.qampus.question.domain.Question;

import java.util.List;
import java.util.Optional;

public interface QuestionCustomRepository {
    List<Question> findByCategoryId(Long categoryId, Pageable pageable, String sort);
    List<Question> searchByKeyword(String value, String sort, Pageable pageable);
    List<Question> findWeeklyPopularQuestions();
    List<Question> findMyQuestions(Long userId, Long categoryId, String sort, Pageable pageable);
}
