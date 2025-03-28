package swyp.qampus.answer.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import swyp.qampus.exception.ErrorCode;
@RequiredArgsConstructor
@Getter
public enum AnswerErrorCode implements ErrorCode {
    DUPLICATED_CHOSEN(HttpStatus.CONFLICT,"이미 채택된 답변입니다."),
    DUPLICATED_NO_CHOSEN(HttpStatus.CONFLICT,"채택이 되지 않은 답변입니다. 취소가 불가능합니다."),
    DUPLICATED_CHOSEN_OF_QUESTION(HttpStatus.BAD_REQUEST,"이 질문에는 이미 채택된 답변이 있습니다."),
    NOT_EXIST_ANSWER(HttpStatus.NOT_FOUND,"답변이 존재하지 않습니다.");
    private final HttpStatus httpStatus;
    private final String message;
    //
}
