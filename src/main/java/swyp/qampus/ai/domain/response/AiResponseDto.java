package swyp.qampus.ai.domain.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import swyp.qampus.ai.domain.Ai;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Builder
@Schema(name = "AI답변 Dto")
public class AiResponseDto {
    @Schema(description = "ai아이디")
    private  Long ai_id;
    @Schema(description = "생성 날짜")
    private String created_date;
    @Schema(description = "답변 내용")
    private String content;

    public static AiResponseDto of(Ai ai){
        return AiResponseDto.builder()
                .created_date(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                .ai_id(ai.getAiId())
                .content(ai.getContent())
                .build();
    }
}
