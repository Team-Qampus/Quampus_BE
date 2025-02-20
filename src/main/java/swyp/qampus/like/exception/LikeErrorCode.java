package swyp.qampus.like.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import swyp.qampus.exception.ErrorCode;

@Getter
@RequiredArgsConstructor
public enum LikeErrorCode implements ErrorCode {
    DUPLICATED_LIKE_REQUEST(HttpStatus.CONFLICT,"중복된 좋아요 리소스 요청입니다."),
    NOT_EXISTED_ANSWERED(HttpStatus.NOT_FOUND,"답변이 존재하지 않습니다."),
    NOT_EXISTED_LIKE(HttpStatus.CONFLICT,"좋아요가 존재하지 않습니다." );
    private final HttpStatus httpStatus;
    private final String message;

}
