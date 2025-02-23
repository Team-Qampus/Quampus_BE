package swyp.qampus.university.repository;

import swyp.qampus.university.domain.response.UniversityRankResponseDto;

import java.util.List;
import java.util.Optional;

public interface UniversityRepositoryCustom {
    Optional<List<UniversityRankResponseDto>> getUniversityRanking(Integer limit,String period);
}
