package swyp.qampus.like.repository;

import swyp.qampus.like.domain.Like;

import java.util.Optional;

public interface LikeRepositoryCustom {
    Optional<Like> findLikesByAnswerAndUser(Long answerId,Long userId);
}
