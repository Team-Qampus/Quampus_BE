package swyp.qampus.ai.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import swyp.qampus.exception.ErrorCode;
@Getter
@RequiredArgsConstructor
public enum AiErrorCode implements ErrorCode {
    NETWORK_ERROR(HttpStatus.INTERNAL_SERVER_ERROR,"AI네트워크 에러입니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
