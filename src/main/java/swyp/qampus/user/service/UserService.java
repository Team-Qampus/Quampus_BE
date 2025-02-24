package swyp.qampus.user.service;

import org.springframework.data.domain.Pageable;
import swyp.qampus.question.domain.MyQuestionResponseDto;

import java.util.List;

public interface UserService {
    List<MyQuestionResponseDto> getMyQuestions(Long userId, Long categoryId, String sort, Pageable pageable);
}
