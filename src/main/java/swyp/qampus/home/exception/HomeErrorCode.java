package swyp.qampus.home.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import swyp.qampus.exception.ErrorCode;

@RequiredArgsConstructor
@Getter
public enum HomeErrorCode implements ErrorCode {
    NOT_FIND_WEEKLY_QNA(HttpStatus.NOT_FOUND, "금주의 인기 질문 또는 답변을 찾을 수 없습니다.");

    private final HttpStatus httpStatus;
    private final String message;
}