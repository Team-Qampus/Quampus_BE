package swyp.qampus.ai.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import swyp.qampus.ai.domain.Ai;

public interface AiRepository extends JpaRepository<Ai,Long> {
}
