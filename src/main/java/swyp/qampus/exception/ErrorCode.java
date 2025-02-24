package swyp.qampus.exception;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.http.HttpStatus;
@Schema(description = "에러메시지 반환 Dto")
public interface ErrorCode {
    String name();
    @Schema(description = "에러 코드 입니다.")
    HttpStatus getHttpStatus();
    @Schema(description = "에러 메시지 입니다.")
    String getMessage();
}