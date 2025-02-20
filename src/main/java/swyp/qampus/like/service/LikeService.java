package swyp.qampus.like.service;

public interface LikeService {
    void insert(String token,Long answerId);
    void delete(String token,Long answerId);
}
