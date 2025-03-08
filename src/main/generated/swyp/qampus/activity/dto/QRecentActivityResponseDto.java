package swyp.qampus.activity.dto;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.ConstructorExpression;
import javax.annotation.processing.Generated;

/**
 * swyp.qampus.activity.dto.QRecentActivityResponseDto is a Querydsl Projection type for RecentActivityResponseDto
 */
@Generated("com.querydsl.codegen.DefaultProjectionSerializer")
public class QRecentActivityResponseDto extends ConstructorExpression<RecentActivityResponseDto> {

    private static final long serialVersionUID = -237809232L;

    public QRecentActivityResponseDto(com.querydsl.core.types.Expression<String> major, com.querydsl.core.types.Expression<swyp.qampus.activity.ActivityType> activityType, com.querydsl.core.types.Expression<Long> id, com.querydsl.core.types.Expression<Long> activityId) {
        super(RecentActivityResponseDto.class, new Class<?>[]{String.class, swyp.qampus.activity.ActivityType.class, long.class, long.class}, major, activityType, id, activityId);
    }

}

