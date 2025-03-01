package swyp.qampus.question.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@RequiredArgsConstructor
@ToString
public class CreateQuestionResponseDto {
    @Schema(description = "요청 성공 여부")
    private final boolean success;

    @Schema(description = "HTTP 상태 코드")
    private final int status;

    @Schema(description = "응답 메시지")
    private final String message;

    @Schema(description = "생성된 질문의 ID")
    private final Long questionId;

    public static CreateQuestionResponseDto of(Boolean success, Integer code, String message, Long questionId){
        return new CreateQuestionResponseDto(success,code,message, questionId);
    }
}
