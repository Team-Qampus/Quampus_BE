package swyp.qampus.answer.service;


import org.springframework.web.multipart.MultipartFile;
import swyp.qampus.answer.domain.AnswerListResponseDto;
import swyp.qampus.answer.domain.AnswerRequestDto;
import swyp.qampus.answer.domain.AnswerUpdateRequestDto;
import swyp.qampus.answer.domain.ChoiceRequestDto;

import java.util.List;

public interface AnswerService {
    void createAnswer(AnswerRequestDto requestDto, List<MultipartFile> images);
    void updateAnswer(Long answer_id, AnswerUpdateRequestDto requestDto);
    void deleteAnswer(Long answer_id);
    void choice(ChoiceRequestDto choiceRequestDto, String token);
    List<AnswerListResponseDto> getQuestions(String sort, Long categoryId, int page, int size);
}
