package swyp.qampus.login.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import swyp.qampus.login.entity.User;
import swyp.qampus.university.repository.UniversityRepositoryCustom;

import java.util.Optional;
@Repository
public interface UserRepository extends JpaRepository<User, Long>, UserRepositoryCustom {

    Optional<User> findByEmail(String email);
}
