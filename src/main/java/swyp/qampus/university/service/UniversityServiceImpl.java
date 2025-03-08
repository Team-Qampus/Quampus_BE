package swyp.qampus.university.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import swyp.qampus.exception.RestApiException;
import swyp.qampus.login.repository.UserRepository;
import swyp.qampus.login.util.JWTUtil;
import swyp.qampus.university.domain.University;
import swyp.qampus.university.domain.response.UniversityDetailResponseDto;
import swyp.qampus.university.domain.response.UniversityRankResponseDto;
import swyp.qampus.university.exception.UniversityErrorCode;
import swyp.qampus.university.repository.UniversityRepository;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@Slf4j
@RequiredArgsConstructor
public class UniversityServiceImpl implements UniversityService {
    private final UniversityRepository universityRepository;
    private final UserRepository userRepository;
    private final JWTUtil jwtUtil;

    @Override
    public Optional<List<UniversityRankResponseDto>> getUniversityRanking( Integer limit, String period) {
        /**/
        return universityRepository.getUniversityRanking(limit, period);
    }

    @Override
    public UniversityDetailResponseDto getUniversityDetail( String universityName) {
        boolean exists=universityRepository.findByUniversityName(universityName).isPresent();
        if(!exists){
            return null;
        }

        return universityRepository.getUniversityDetail(universityName)
                .orElse(null);
    }

    @Override
    @Transactional
    @Scheduled(cron = "1 0 0 1 * * ")
    public void resetMonthly() {
        universityRepository.resetMonthlyChoiceCnt();
        log.info("monthlyChoice 초기화");
    }

    @Override
    @Transactional
    @Scheduled(cron = "59 59 23 * * 7")
    public void resetWeekly() {
        universityRepository.resetWeeklyChoiceCnt();
        log.info("weeklyChoice 초기화");
    }


}
