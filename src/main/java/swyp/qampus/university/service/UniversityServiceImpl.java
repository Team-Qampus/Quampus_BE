package swyp.qampus.university.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import swyp.qampus.exception.RestApiException;
import swyp.qampus.login.util.JWTUtil;
import swyp.qampus.common.kafka.RecentUniversityActivityType;
import swyp.qampus.university.domain.University;
import swyp.qampus.university.domain.response.RecentActivityResponseDto;
import swyp.qampus.university.domain.response.UniversityDetailResponseDto;
import swyp.qampus.university.domain.response.UniversityRankResponseDto;
import swyp.qampus.university.exception.UniversityErrorCode;
import swyp.qampus.university.repository.UniversityRepository;

import java.util.*;

@Service
@Transactional(readOnly = true)
@Slf4j
@RequiredArgsConstructor
public class UniversityServiceImpl implements UniversityService {
    private final UniversityRepository universityRepository;
    private final JWTUtil jwtUtil;
    private final ObjectMapper objectMapper;
    private static final String TOPIC_NAME="university_*";

    //최신 5개 메시지만 저장
    private final Queue<HashMap<String,Object>> recentActivities=new LinkedList<>();

    @Override
    public Optional<List<UniversityRankResponseDto>> getUniversityRanking(String token, Integer limit, String period) {
        /**/
        return universityRepository.getUniversityRanking(limit, period);
    }

    @Override
    public Optional<UniversityDetailResponseDto> getUniversityDetail(String token, String universityName) {
        return universityRepository.getUniversityDetail(universityName);
    }

    @Override
    public Optional<List<RecentActivityResponseDto>> getRecentActivity(String token, String universityName) throws JsonProcessingException {
        //대학교 조회
        University university=universityRepository.findByUniversityName(universityName)
                .orElseThrow(()->new RestApiException(UniversityErrorCode.NOT_EXIST_UNIVERSITY));


        //최근 5개 메시지 반환
        List<RecentActivityResponseDto> responseDtoList=recentActivities.stream()
                .map(data->RecentActivityResponseDto
                        .builder()
                        .department_name((String) data.get("departName"))
                        .id((Long) data.get("id"))
                        .type((RecentUniversityActivityType) data.get("type"))
                        .updated_time((String) data.get("time"))
                        .build())
                .toList();

        return Optional.of(responseDtoList);

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

    //Kafka 메시지 중 해당 대학교와 일치 값 추출
    @KafkaListener(topics = TOPIC_NAME,groupId = "university-group")
    private void extractCorrectUniversityName(String message) {
        try {
            HashMap<String,Object> hashMap = objectMapper.readValue(message, HashMap.class);

            //동시성 제어
            synchronized (recentActivities){
                if(recentActivities.size()>=4){
                    recentActivities.poll();
                }
                recentActivities.offer(hashMap);
            }
            log.info("Received Kafka message: {}", hashMap);
        }catch (JsonProcessingException e){
            log.error("Error Kafka message",e);
        }
    }

}
