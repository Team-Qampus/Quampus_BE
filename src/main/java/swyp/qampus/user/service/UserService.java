package swyp.qampus.user.service;

import swyp.qampus.question.domain.MyQuestionResponseDto;

import java.util.List;

public interface UserService {
    List<MyQuestionResponseDto> getMyQuestions(Long userId, Long categoryId, String sort, int page, int size);
}
