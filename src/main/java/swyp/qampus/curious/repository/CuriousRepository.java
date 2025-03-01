package swyp.qampus.curious.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import swyp.qampus.curious.domain.Curious;


public interface CuriousRepository extends JpaRepository<Curious,Long>,CuriousCustomRepository {
    boolean existsByUserUserIdAndQuestionQuestionId(Long userId, Long questionId);
}
