package swyp.qampus.answer.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import swyp.qampus.question.domain.CreateQuestionResponseDto;

@Getter
@ToString
@RequiredArgsConstructor
public class CreateAnswerResponseDto {
    private final boolean success;

    private final int status;

    private final String message;

    private final Long answerId;

    public static CreateAnswerResponseDto of(Boolean success,Integer code,String message,Long answerId){
        return new CreateAnswerResponseDto(success,code,message,answerId);
    }
}
