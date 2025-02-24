package swyp.qampus.question.service;

import org.springframework.web.multipart.MultipartFile;
import swyp.qampus.question.domain.QuestionRequestDto;
import swyp.qampus.question.domain.QuestionUpdateRequestDto;

import java.util.List;

public interface QuestionService {
    void createQuestion(Long user_id, QuestionRequestDto requestDto, List<MultipartFile> images);
    void updateQuestion(Long question_id, QuestionUpdateRequestDto requestDto);
    void deleteQuestion(Long question_id);
}
