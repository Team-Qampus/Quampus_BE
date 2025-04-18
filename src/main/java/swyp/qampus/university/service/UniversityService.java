package swyp.qampus.university.service;

import swyp.qampus.university.domain.response.UniversityDetailResponseDto;
import swyp.qampus.university.domain.response.UniversityRankResponseDto;

import java.util.List;
import java.util.Optional;

public interface UniversityService {
    Optional<List<UniversityRankResponseDto>>getUniversityRanking(Integer limit,String period);
    //대학교 상세보기
    UniversityDetailResponseDto getUniversityDetail( String universityName);
    //매달 채택 수 초기화 스케쥴링
    void resetMonthly();
    //매주 채택 수 초기화 스케쥴링
    void resetWeekly();

    void changeUniversityName(String universityName, Long universityId);
}
