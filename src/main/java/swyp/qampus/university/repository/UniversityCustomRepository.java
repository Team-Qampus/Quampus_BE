package swyp.qampus.university.repository;

import swyp.qampus.university.domain.response.UniversityRankResponseDto;

import java.util.List;

public interface UniversityCustomRepository {
    List<UniversityRankResponseDto> getUniversityList(String universityName,int limit);
}
