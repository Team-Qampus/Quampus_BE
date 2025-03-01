package swyp.qampus.university.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import swyp.qampus.university.domain.response.RecentActivityResponseDto;
import swyp.qampus.university.domain.response.UniversityDetailResponseDto;
import swyp.qampus.university.domain.response.UniversityRankResponseDto;

import java.util.List;
import java.util.Optional;

public interface UniversityService {
    Optional<List<UniversityRankResponseDto>>getUniversityRanking(Integer limit,String period);
    //대학교 상세보기
    Optional<UniversityDetailResponseDto> getUniversityDetail( String universityName);
    //매달 채택 수 초기화 스케쥴링
    void resetMonthly();
    //매주 채택 수 초기화 스케쥴링
    void resetWeekly();
    //최근 활동 조회
    Optional<List<RecentActivityResponseDto>>getRecentActivity(String token,String universityName) throws JsonProcessingException;
}
