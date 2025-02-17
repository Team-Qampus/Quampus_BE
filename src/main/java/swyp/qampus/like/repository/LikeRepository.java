package swyp.qampus.like.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import swyp.qampus.like.domain.Like;

public interface LikeRepository extends JpaRepository<Like,Long>,LikeRepositoryCustom {
}
