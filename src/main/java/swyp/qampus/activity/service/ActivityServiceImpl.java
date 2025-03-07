package swyp.qampus.activity.service;

import lombok.RequiredArgsConstructor;
import swyp.qampus.activity.dto.RecentActivityResponseDto;
import swyp.qampus.login.config.data.RedisCustomService;
import swyp.qampus.login.util.JWTUtil;

import java.util.List;
@RequiredArgsConstructor
public class ActivityServiceImpl implements ActivityService{
    private final JWTUtil jwtUtil;
    private final RedisCustomService redisCustomService;
    @Override
    public List<RecentActivityResponseDto> getRecentActivities(String token, String universityName) {
        return null;
    }
}
