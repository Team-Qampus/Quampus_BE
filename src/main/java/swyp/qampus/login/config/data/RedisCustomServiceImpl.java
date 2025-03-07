package swyp.qampus.login.config.data;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Service
public class RedisCustomServiceImpl implements RedisCustomService {

    private final RedisTemplate<String, Object> redisTemplate;

    public RedisCustomServiceImpl(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
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
        //데이터가 5개 이상이면 삭제
        Long size = redisTemplate.opsForList().size(keyName);

        if (size != null && size >= 5) {
            //data가 5개이상이면 가장 오래된 데이터 삭제
            redisTemplate.opsForList().leftPop(keyName);
        }
        redisTemplate.opsForList().rightPush(keyName,map);
        redisTemplate.expire(keyName,limitTime,TimeUnit.HOURS);
    }

    //특정 키값을 가진 레디스 데이터 개수 조회
    @Override
    public Long getRedisDataCount(String keyName) {
        return redisTemplate.opsForList().size(keyName);
    }
}
