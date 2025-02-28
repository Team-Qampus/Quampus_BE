package swyp.qampus.university.repository;

import swyp.qampus.university.domain.response.UniversityDetailResponseDto;
import swyp.qampus.university.domain.response.UniversityRankResponseDto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface UniversityRepositoryCustom {
    //학교랭킹 조회
    Optional<List<UniversityRankResponseDto>> getUniversityRanking(Integer limit,String period);

    //학교 이름으로 학교 조회 및 상세보기
    Optional<UniversityDetailResponseDto> getUniversityDetail(String universityName);
    //매달 1일 월별 채택 수 초기화
    void resetMonthlyChoiceCnt();
    //매주 일요일 23시 59분 59초 채택 수 초기화
    void resetWeeklyChoiceCnt();
    //월별 학교 순위 조회-날짜 입력
    int getThisMonthRankOfSchool(String universityName);

}
