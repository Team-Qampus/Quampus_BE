package swyp.qampus.home.service;

import swyp.qampus.home.dto.HomeResponseDto;

public interface HomeService {
    void updateWeeklyPopularQna();
    HomeResponseDto getUserHomeContent(String token);
    HomeResponseDto getGuestHomeContent();
}
