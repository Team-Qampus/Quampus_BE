package swyp.qampus.ai.domain.response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import swyp.qampus.ai.domain.Ai;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Builder
public class AiResponseDto {
    private  Long ai_id;
    private String created_date;
    private String content;

    public static AiResponseDto of(Ai ai){
        return AiResponseDto.builder()
                .created_date(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                .ai_id(ai.getAiId())
                .content(ai.getContent())
                .build();
    }
}
