package swyp.qampus.login.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import swyp.qampus.login.entity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);
}
