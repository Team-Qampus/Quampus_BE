package swyp.qampus.university.service;

import swyp.qampus.university.domain.response.UniversityDetailResponseDto;
import swyp.qampus.university.domain.response.UniversityRankResponseDto;

import java.util.List;
import java.util.Optional;

public interface UniversityService {
    Optional<List<UniversityRankResponseDto>>getUniversityRanking(String token,Integer limit,String period);
    //대학교 상세보기
    Optional<UniversityDetailResponseDto> getUniversityDetail(String token, String universityName);
    //매달 채택 수 초기화 스케쥴링
    void resetMonthly();
    //매주 채택 수 초기화 스케쥴링
    void resetWeekly();
    //매달 유저 채택 수 초기화 스케쥴링
    void userResetMonthly();
    //매주 유저 채택 수 초기화 스케쥴링
    void userResetWeekly();
}
