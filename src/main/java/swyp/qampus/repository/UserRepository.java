package swyp.qampus.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import swyp.qampus.entity.User;

public interface UserRepository extends JpaRepository <User, String> {
}
