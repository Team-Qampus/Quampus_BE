package swyp.qampus.university.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import swyp.qampus.exception.ErrorCode;

@Getter
@RequiredArgsConstructor
public enum UniversityErrorCode implements ErrorCode {
    NOT_EXIST_UNIVERSITY(HttpStatus.NOT_FOUND,"학교가 존재하지 않습니다.");
    private final HttpStatus httpStatus;
    private final String message;
}
