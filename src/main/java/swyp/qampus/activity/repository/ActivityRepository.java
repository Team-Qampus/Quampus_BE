package swyp.qampus.activity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import swyp.qampus.activity.Activity;
@Repository
public interface ActivityRepository extends JpaRepository<Activity,Long>,ActivityCustomRepository {
}
