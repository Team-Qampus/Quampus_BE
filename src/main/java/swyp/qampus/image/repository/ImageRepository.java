package swyp.qampus.image.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import swyp.qampus.answer.domain.Answer;
import swyp.qampus.image.domain.Image;
import swyp.qampus.question.domain.Question;

import java.util.List;

public interface ImageRepository extends JpaRepository<Image,Long> {
    List<Image> findByQuestion(Question question);
    List<Image> findByAnswer(Answer answer);
}