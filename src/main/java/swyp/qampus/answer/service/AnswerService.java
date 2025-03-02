package swyp.qampus.answer.service;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;
import swyp.qampus.question.domain.QuestionDetailResponseDto;
import swyp.qampus.question.domain.QuestionListResponseDto;
import swyp.qampus.answer.domain.AnswerRequestDto;
import swyp.qampus.answer.domain.AnswerUpdateRequestDto;
import swyp.qampus.answer.domain.ChoiceRequestDto;
import swyp.qampus.question.domain.QuestionDetailResponseDto;
import swyp.qampus.question.domain.QuestionListResponseDto;
import swyp.qampus.question.domain.QuestionResponseDto;

import java.util.List;

public interface AnswerService {
    void createAnswer(AnswerRequestDto requestDto, List<MultipartFile> images,String token);
    void updateAnswer(Long answer_id, AnswerUpdateRequestDto requestDto,String token);
    void deleteAnswer(Long answer_id,String token);
    void choice(ChoiceRequestDto choiceRequestDto, String token);
    Page<QuestionListResponseDto> getQuestions(Long categoryId, String sort, Pageable pageable, String token);
    QuestionDetailResponseDto getQuestionDetail(Long questionId, String token);
    Page<QuestionResponseDto> searchQuestions(String value, String sort, Pageable pageable,String token);
}
