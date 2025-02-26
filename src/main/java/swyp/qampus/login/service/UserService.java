package swyp.qampus.login.service;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import swyp.qampus.question.domain.MyQuestionResponseDto;

import java.util.List;


public interface UserService {
    List<MyQuestionResponseDto> getMyQuestions(String token, Long categoryId, String sort, Pageable pageable);
}
