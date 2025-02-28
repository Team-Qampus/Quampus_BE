package swyp.qampus.login.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@RequiredArgsConstructor
@ToString
public class TokenResponseDto {
    @Schema(description = "성공 유무 반환")
    private final Boolean success;
    @Schema(description = "성공 코드")
    private final Integer code;
    @Schema(description = "성공 메시지")
    private final String message;
    @Schema(description = "테스트 토큰")
    private final String data;

    public static TokenResponseDto of(Boolean success, Integer code, String message, String data){
        return new TokenResponseDto(success, code, message, data);
    }
}