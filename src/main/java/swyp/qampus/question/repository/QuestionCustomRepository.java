package swyp.qampus.question.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import swyp.qampus.ai.domain.Ai;
import swyp.qampus.login.entity.User;
import swyp.qampus.question.domain.Question;

import java.util.List;
import java.util.Optional;

public interface QuestionCustomRepository {
    Page<Question> findByCategoryId(Long categoryId, Pageable pageable, String sort);
    Page<Question> searchByKeyword(String value, String sort, Pageable pageable);
    List<Question> findWeeklyPopularQuestions();
    Page<Question> findMyQuestions(User user, Long categoryId, String sort, Pageable pageable);
}
