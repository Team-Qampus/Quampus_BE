package swyp.qampus.university.repository;

import swyp.qampus.university.domain.response.UniversityDetailResponseDto;
import swyp.qampus.university.domain.response.UniversityRankResponseDto;

import java.util.List;
import java.util.Optional;

public interface UniversityRepositoryCustom {
    //학교랭킹 조회
    Optional<List<UniversityRankResponseDto>> getUniversityRanking(Integer limit,String period);

    //학교 이름으로 학교 조회 및 상세보기
    Optional<UniversityDetailResponseDto> getUniversityDetail(String universityName);
}
