package swyp.qampus.common;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.aspectj.apache.bcel.classfile.Code;

@Getter
@RequiredArgsConstructor
@ToString
@Schema(description = "성공 Response Dto")
public class ResponseDto {
    @Schema(description = "성공 유무 반환")
    private final Boolean success;
    @Schema(description = "성공 코드")
    private final Integer code;
    @Schema(description = "성공 메시지")
    private final String message;

    public static ResponseDto of(Boolean success, Integer code,String message){
        return new ResponseDto(success,code,message);
    }
}

