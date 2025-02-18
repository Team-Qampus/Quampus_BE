package swyp.qampus.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.aspectj.apache.bcel.classfile.Code;

@Getter
@RequiredArgsConstructor
@ToString
public class ResponseDto {
    private final Boolean success;
    private final Integer code;
    private final String message;

    public static ResponseDto of(Boolean success, Integer code,String message){
        return new ResponseDto(success,code,message);
    }
}