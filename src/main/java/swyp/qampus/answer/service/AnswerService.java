package swyp.qampus.answer.service;

import swyp.qampus.answer.domain.AnswerRequestDto;
import swyp.qampus.answer.domain.AnswerResponseDto;
import swyp.qampus.answer.domain.AnswerUpdateRequestDto;
import swyp.qampus.common.ResponseDto;
import swyp.qampus.question.domain.MessageResponseDto;

public interface AnswerService {
    AnswerResponseDto createAnswer(AnswerRequestDto requestDto);
    MessageResponseDto updateAnswer(Long answer_id, AnswerUpdateRequestDto requestDto);
    MessageResponseDto deleteAnswer(Long answer_id);
    void choice(Long answerId,Long questId,String token);
}
