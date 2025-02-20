package swyp.qampus.question.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import swyp.qampus.exception.ErrorCode;
@RequiredArgsConstructor
@Getter
public enum QuestionErrorCode implements ErrorCode {
    NOT_EXIST_QUESTION(HttpStatus.NOT_FOUND,"질문이 존재하지 않습니다.");
    private final HttpStatus httpStatus;
    private final String message;
}
