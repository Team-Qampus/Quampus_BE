package swyp.qampus.activity.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import swyp.qampus.activity.ActivityType;

@NoArgsConstructor
@Getter

public class RecentActivityResponseDto {
    private String major;
    private ActivityType activityType;
    private Long id;
    private Long activityId;

    @QueryProjection
    public RecentActivityResponseDto(String major,ActivityType activityType,Long id,Long activityId){
        this.activityId=activityId;
        this.activityType=activityType;
        this.id=id;
        this.major=major;
    }
}
