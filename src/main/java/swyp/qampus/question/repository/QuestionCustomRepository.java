package swyp.qampus.question.repository;

import org.springframework.data.domain.Pageable;
import swyp.qampus.question.domain.Question;

import java.util.List;

public interface QuestionCustomRepository {
    List<Question> findByCategoryId(Long categoryId, Pageable pageable, String sort);
    List<Question> findAllPaged(Pageable pageable, String sort);
    List<Question> searchByKeyword(String value, String sort, Pageable pageable);
}
