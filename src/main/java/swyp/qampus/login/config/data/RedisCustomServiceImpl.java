package swyp.qampus.login.config.data;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Service
public class RedisCustomServiceImpl implements RedisCustomService {

    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper;

    public RedisCustomServiceImpl(RedisTemplate<String, Object> redisTemplate,ObjectMapper objectMapper) {
        this.redisTemplate = redisTemplate;
        this.objectMapper=objectMapper;
    }

    @Override
    public void saveRedisData(String keyName, String value, Long limitTime) {
        redisTemplate.opsForValue().set(keyName, value, limitTime, TimeUnit.SECONDS);
    }

    @Override
    public String getRedisData(String key) {
        return (String) redisTemplate.opsForValue().get(key);
    }

    @Override
    public void deleteRedisData(String key) {
        redisTemplate.delete(key);
    }

    @Override
    public boolean hasKey(String key) {
        return redisTemplate.hasKey(key);
    }

    @Override
    public Set<String> getKeysByPattern(String pattern) {
        return redisTemplate.keys(pattern);
    }

    @Override
    public void saveRedisDataForActivity(String keyName, HashMap<String, Object> map, Long limitTime) {
        try {
            // HashMap -> JSON 문자열로 변환
            String jsonString = objectMapper.writeValueAsString(map);

            // 데이터가 5개 이상이면 삭제
            Long size = redisTemplate.opsForList().size(keyName);
            if (size != null && size >= 5) {
                redisTemplate.opsForList().leftPop(keyName);
            }
            redisTemplate.opsForList().rightPush(keyName, jsonString);
            redisTemplate.expire(keyName, limitTime, TimeUnit.HOURS);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //특정 키값을 가진 레디스 데이터 개수 조회
    @Override
    public Long getRedisDataCount(String keyName) {
        return redisTemplate.opsForList().size(keyName);
    }

    @Override
    public HashMap<String, Object> getRedisDataForMap(String keyName) {
       try {
           String jsonString=(String) redisTemplate.opsForValue().get(keyName);
           if(jsonString==null){
               //Json-> HashMap
               return new HashMap<>();
           }
           return objectMapper.readValue(jsonString, new TypeReference<HashMap<String, Object>>() {});
       }catch (Exception e){
           e.printStackTrace();
       }
       return new HashMap<>();
    }

    @Override
    public List<HashMap<String, Object>> getRedisDataForList(String redisKey) {
        ListOperations<String, Object> listOperations = redisTemplate.opsForList();
        List<Object> redisData = listOperations.range(redisKey, 0, -1); // 0에서 끝까지 범위 지정

        List<HashMap<String, Object>> result = new ArrayList<>();

        for (Object data : redisData) {
            // Redis에 저장된 데이터를 HashMap으로 변환
            HashMap<String, Object> hashMap = convertDataToHashMap((String) data);
            result.add(hashMap);
        }
        return result;

    }
    private HashMap<String, Object> convertDataToHashMap(String data) {
        // 데이터 변환 로직 (예: JSON 형태로 저장된 데이터를 HashMap으로 변환)
        // 여기에 Jackson을 사용하여 JSON 문자열을 HashMap으로 변환하는 예시
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(data, HashMap.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error converting Redis data to HashMap", e);
        }
    }
}
