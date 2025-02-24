package Quampus.demo.login.config.data;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.concurrent.TimeUnit;

@Service
public class RedisCustomServiceImpl implements RedisCustomService {

    private final RedisTemplate<String,Object> redisTemplate;

    public RedisCustomServiceImpl(RedisTemplate<String,Object> redisTemplate){
        this.redisTemplate=redisTemplate;
    }

    @Override
    public void saveRedisData(String keyName, String value, Long limitTime) {
        redisTemplate.opsForValue().set(keyName,value,limitTime, TimeUnit.SECONDS);
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
}
