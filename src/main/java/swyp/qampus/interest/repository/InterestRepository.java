package swyp.qampus.interest.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import swyp.qampus.interest.domain.Interest;

public interface InterestRepository extends JpaRepository<Interest,Long>,InterestCustomRepository {
}
