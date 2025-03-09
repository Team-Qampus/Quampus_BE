package swyp.qampus.activity.service;

import org.springframework.stereotype.Service;
import swyp.qampus.activity.dto.RecentActivityResponseDto;

import java.util.List;

@Service
public interface ActivityService {
    List<RecentActivityResponseDto> getRecentActivities(String universityName);
}
