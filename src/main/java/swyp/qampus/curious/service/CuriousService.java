package swyp.qampus.curious.service;

public interface CuriousService {
    void insert(String token,Long questionId);
    void delete(String token,Long questionId);
}
