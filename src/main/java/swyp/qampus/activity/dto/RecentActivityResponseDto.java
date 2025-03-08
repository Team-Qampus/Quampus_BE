package swyp.qampus.activity.dto;

import com.querydsl.core.annotations.QueryProjection;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import swyp.qampus.activity.ActivityType;

@NoArgsConstructor
@Getter
@Builder
@Schema(name = "학교별 최신 활동 5개 조회Dto")
public class RecentActivityResponseDto {
    @Schema(description = "전공명", example = "Computer Science")
    private String major;

    @Schema(description = "활동 유형 QUESTION:답변 등록 / ANSWER:질문 등록 / CHOICE_SAVE:채택 등록 / CHOICE_DELETE:채택 취소", example = "LECTURE")
    private ActivityType activityType;

    @Schema(description = "activeType별 id값 예)activityType:QUESTION id:1 이면 질문의 answer_id값이 1입니다.", example = "123")
    private Long id;

    @Schema(description = "활동 고유 Index", example = "456")
    private Long activityId;

    @QueryProjection
    public RecentActivityResponseDto(String major,ActivityType activityType,Long id,Long activityId){
        this.activityId=activityId;
        this.activityType=activityType;
        this.id=id;
        this.major=major;
    }
}
