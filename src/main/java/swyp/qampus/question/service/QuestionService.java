package swyp.qampus.question.service;

import org.springframework.web.multipart.MultipartFile;
import swyp.qampus.question.domain.QuestionRequestDto;
import swyp.qampus.question.domain.QuestionUpdateRequestDto;

import java.util.List;

public interface QuestionService {
    Long createQuestion( QuestionRequestDto requestDto, List<MultipartFile> images,String token);
    void updateQuestion(Long question_id, QuestionUpdateRequestDto requestDto, List<MultipartFile> images, String token);
    void deleteQuestion(Long question_id,String token);
}
