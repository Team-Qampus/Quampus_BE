package swyp.qampus.activity.repository;

import swyp.qampus.activity.dto.RecentActivityResponseDto;

import java.util.List;

public interface ActivityCustomRepository {
    List<RecentActivityResponseDto> getRecentActivityOrderByRecent(Long activityId,Integer count,Long universityId);
}
