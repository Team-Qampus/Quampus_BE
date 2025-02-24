package swyp.qampus.university.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import swyp.qampus.login.entity.User;
@Repository
public interface UniversityRepository extends JpaRepository<User,String> , UniversityCustomRepository {
}
