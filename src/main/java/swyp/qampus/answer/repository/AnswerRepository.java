package swyp.qampus.answer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import swyp.qampus.answer.domain.Answer;

import java.util.List;

public interface AnswerRepository extends JpaRepository<Answer, Long>,AnswerCustomRepository {
    // 특정 질문에 대한 모든 답변 조회
    List<Answer> findByQuestionQuestionId(Long questionId);
}
