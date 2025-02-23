package swyp.qampus.university.service;

import swyp.qampus.university.domain.response.UniversityRankResponseDto;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

public interface UniversityService {
    Optional<List<UniversityRankResponseDto>>getUniversityRanking(String token,Integer limit,String period);
}
