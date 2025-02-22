package swyp.qampus.university.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import swyp.qampus.university.domain.University;

public interface UniversityRepository extends JpaRepository<University,Long> {
}
