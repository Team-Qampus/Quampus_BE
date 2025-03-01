package swyp.qampus.university.domain.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import swyp.qampus.data.kafka.RecentUniversityActivityType;

@Getter
@Schema(name = "최근 활동 Dto")
@Builder
public class RecentActivityResponseDto {
    @Schema(description = "질문/답변 id")
    private Long id;

    @Schema(description = "질문 작성/답변 작성/답변 채택/질문 좋아요/질문 나도 궁금해요 타입")
    private RecentUniversityActivityType type;

    @Schema(description = "학과")
    private String department_name;

    @Schema(description = "시간 기록")
    private String updated_time;
}
