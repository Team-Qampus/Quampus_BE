package swyp.qampus.question.repository;

import swyp.qampus.answer.domain.Answer;
import swyp.qampus.question.domain.Question;

import java.util.List;

public interface QuestionCustomRepository {
    List<Question> findByCategoryId(Long categoryId, int page, int size, String sort);
    List<Question> findAllPaged(int page, int size, String sort);
    List<Question> searchByKeyword(String value, String sort, int page, int size);
    List<Question> findWeeklyPopularQuestions();
}
