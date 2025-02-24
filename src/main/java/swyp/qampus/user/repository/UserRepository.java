package swyp.qampus.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import swyp.qampus.user.domain.User;

public interface UserRepository extends JpaRepository <User, Long> {
}
