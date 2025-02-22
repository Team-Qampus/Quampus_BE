package swyp.qampus.question.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import swyp.qampus.question.domain.Question;

import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, Long> {

    // 특정 사용자가 작성한 질문 조회
    List<Question> findByUserUserId(Long userId);

    // 특정 카테고리에 속한 질문 조회
    List<Question> findByCategoryCategoryId(Long categoryId);

}
