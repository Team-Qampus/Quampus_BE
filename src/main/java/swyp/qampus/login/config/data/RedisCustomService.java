package swyp.qampus.login.config.data;

import java.util.HashMap;
import java.util.Set;

public interface RedisCustomService {
    void saveRedisData(String keyName, String value, Long limitTime) ;
    String getRedisData(String key);
    void deleteRedisData(String key);
    boolean hasKey(String key);
    Set<String> getKeysByPattern(String pattern);
    void saveRedisDataForActivity(String keyName, HashMap<String,Object> map,Long limitTime);
}
