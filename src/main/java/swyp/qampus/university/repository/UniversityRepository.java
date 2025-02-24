package swyp.qampus.university.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import swyp.qampus.university.domain.University;

import java.util.Optional;

public interface UniversityRepository extends JpaRepository<University,Long>,UniversityRepositoryCustom {
    Optional<University>findByUniversityName(String universityName);
}
