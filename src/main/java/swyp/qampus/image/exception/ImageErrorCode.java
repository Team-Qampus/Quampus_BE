package swyp.qampus.image.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import swyp.qampus.exception.ErrorCode;

@Getter
@RequiredArgsConstructor
public enum ImageErrorCode implements ErrorCode {

    FAILED_UPLOAD(HttpStatus.BAD_REQUEST,"이미지 업로드를 실패했습니다.");
    private final HttpStatus httpStatus;
    private final String message;

}
