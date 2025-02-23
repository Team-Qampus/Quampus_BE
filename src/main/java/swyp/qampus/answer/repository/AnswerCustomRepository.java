package swyp.qampus.answer.repository;

import swyp.qampus.answer.domain.Answer;
import swyp.qampus.question.domain.Question;

import java.util.List;

public interface AnswerCustomRepository {
    //답변의 채택 개수 조회(1개)
    Integer countChoiceOfAnswer(Long questionId);
    List<Answer> findWeeklyPopularAnswers();
}
