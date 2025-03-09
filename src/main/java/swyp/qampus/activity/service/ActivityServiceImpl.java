package swyp.qampus.activity.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import swyp.qampus.activity.ActivityType;
import swyp.qampus.activity.dto.RecentActivityResponseDto;
import swyp.qampus.activity.repository.ActivityRepository;
import swyp.qampus.exception.RestApiException;
import swyp.qampus.login.config.data.RedisCustomService;
import swyp.qampus.login.util.JWTUtil;
import swyp.qampus.university.domain.University;
import swyp.qampus.university.exception.UniversityErrorCode;
import swyp.qampus.university.repository.UniversityRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
@RequiredArgsConstructor
@Service
public class ActivityServiceImpl implements ActivityService{
    private final JWTUtil jwtUtil;
    private final RedisCustomService redisCustomService;
    private final UniversityRepository universityRepository;
    private final ActivityRepository activityRepository;
    private final static String REDIS_PREFIX="activity ";
    private final static int LIMIT=5;
    @Override
    @Transactional(readOnly = true)
    public List<RecentActivityResponseDto> getRecentActivities( String universityName) {
        //List<RecentActivityResponseDto> result=new ArrayList<>();
        List<University> universities = universityRepository.findAllByUniversityName(universityName);
        if (universities.isEmpty()) {
            throw new RestApiException(UniversityErrorCode.NOT_EXIST_UNIVERSITY);
        }
        University university = universities.get(0); // 첫 번째 결과 사용

        Long universityId=university.getUniversityId();
        //String redisKey = REDIS_PREFIX + universityId;
        /**
         * Logic Description
         * 1. Redis Service에서 일단 해당 학교Id로 개수를 조회
         * 2. 5개 조회 중에 부족한만큼 Activity DB에 쿼리문을 활용하여 조회
         * 3. 만약 레디스에서 5개 모두 조회하면 DB조회 X
         * 4. DB에서 쿼리문으로 조회 시 레디스에 존재하는 마지막 Index보다 작은 값들 조회
         */
        // Redis에서 전체 리스트 가져오기
        /*int redisSize = Math.toIntExact(redisCustomService.getRedisDataCount(redisKey));
        Long lastIndex = 0L;

        // Redis에서 데이터를 가져옴
        List<HashMap<String, Object>> redisData = redisCustomService.getRedisDataForList(redisKey); // 한 번에 데이터 가져오기

        for (int i = 0; i < redisSize; i++) {
            HashMap<String, Object> hashMap = redisData.get(i);

            // Number를 사용하여 안전하게 형변환
            Number activityIdNum = (Number) hashMap.get("activityId");
            Number idNum = (Number) hashMap.get("id");
            String typeStr = (String) hashMap.get("type");
            ActivityType activityType = ActivityType.valueOf(typeStr);


            RecentActivityResponseDto responseDto = RecentActivityResponseDto.builder()
                    .activityId(activityIdNum.longValue())
                    .activityType(activityType)
                    .major((String) hashMap.get("major"))
                    .id(idNum.longValue())
                    .build();

            result.add(responseDto);
            if (i == redisSize - 1) {
                lastIndex = activityIdNum.longValue();
            }
        }

        //부족한 개수
        int remainCount=LIMIT-redisSize;

        //DB에서 조회
        if(remainCount>0){
            List<RecentActivityResponseDto> dbDataList=activityRepository.getRecentActivityOrderByRecent(lastIndex,remainCount,universityId);
            result.addAll(dbDataList);
        }
        return result;*/

        return activityRepository.getRecentActivityOrderByRecent(Long.MAX_VALUE,5,universityId);
    }
}
