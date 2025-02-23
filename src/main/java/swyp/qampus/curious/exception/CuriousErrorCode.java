package swyp.qampus.curious.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import swyp.qampus.exception.ErrorCode;

@Getter
@RequiredArgsConstructor
public enum CuriousErrorCode implements ErrorCode {
    CAN_NOT_CLICK_MINE(HttpStatus.BAD_REQUEST,"자신의 질문에는 '나도 궁금해요'를 클릭할 수 없습니다."),
    DUPLICATED_CURIOUS_REQUEST(HttpStatus.CONFLICT,"중복된 나도 궁금해요 리소스 요청입니다.");
    private final HttpStatus httpStatus;
    private final String message;

}
