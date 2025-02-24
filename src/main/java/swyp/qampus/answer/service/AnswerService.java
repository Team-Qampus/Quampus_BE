package swyp.qampus.answer.service;


import org.springframework.web.multipart.MultipartFile;
import swyp.qampus.answer.domain.AnswerRequestDto;
import swyp.qampus.answer.domain.AnswerResponseDto;
import swyp.qampus.answer.domain.AnswerUpdateRequestDto;
import swyp.qampus.answer.domain.ChoiceRequestDto;
import swyp.qampus.common.ResponseDto;
import swyp.qampus.question.domain.MessageResponseDto;

import java.util.List;

public interface AnswerService {
    AnswerResponseDto createAnswer(AnswerRequestDto requestDto, List<MultipartFile> images);
    MessageResponseDto updateAnswer(Long answer_id, AnswerUpdateRequestDto requestDto);
    MessageResponseDto deleteAnswer(Long answer_id);
    void choice(ChoiceRequestDto choiceRequestDto, String token);
}
