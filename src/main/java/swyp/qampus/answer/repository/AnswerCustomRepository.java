package swyp.qampus.answer.repository;

public interface AnswerCustomRepository {
    //답변의 채택 개수 조회(1개)
    Integer countChoiceOfAnswer(Long questionId);
}
