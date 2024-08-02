package CoinIndicator.CoinIndicator.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class RedisService {

    private final RedisTemplate<String, Object> redisTemplate;
    private final HashOperations<String, String, String> hashOperations;

    public RedisService(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
        this.hashOperations = redisTemplate.opsForHash();
    }

    /**
     * Redis에 키-값 쌍을 저장.
     *
     * @param key   Redis에 저장할 키.
     * @param value Redis에 저장할 값.
     */
    public void setValue(String key, Object value) {
        redisTemplate.opsForValue().set(key, value);
    }

    /**
     * Redis에 키-해쉬키-값을 저장.
     *
     * @param key   Redis에 저장할 키.
     * @param hashKey Redis에 저장할 해쉬 키.
     * @param value Redis에 저장할 값.
     */
    public void setHashValue(String key, String hashKey, String value) {
        hashOperations.put(key, hashKey, value);
    }

    /**
     * Redis에서 키에 해당하는 값을 조회.
     *
     * @param key Redis에서 조회할 키.
     * @return 키에 해당하는 값, 존재하지 않으면 null을 반환.
     */
    public Object getValue(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    /**
     * Redis에 키-해쉬키에 해당하는 값을 조회.
     *
     * @param key   Redis에서 조회할 키.
     * @param hashKey Redis에서 조회할 해쉬 키.
     * @return 키에 해당하는 값, 존재하지 않으면 null을 반환.
     */
    public String getHashValue(String key, String hashKey) {
        return hashOperations.get(key, hashKey);
    }

    /**
     * 주어진 key에 해당하는 모든 hashKey와 value를 Redis에서 조회.
     *
     * @param key   Redis에서 조회할 키.
     * @return 키에 해당하는 값, 존재하지 않으면 null을 반환.
     */

    public Map<String, String> getHashValues(String key) {
        return hashOperations.entries(key);
    }

    /**
     * 만료 시간을 지정하여 키-값 쌍을 Redis에 저장
     *
     * @param key     Redis에 저장할 키.
     * @param value   Redis에 저장할 값.
     * @param timeout 만료 시간.
     * @param unit    만료 시간의 단위.
     */
    public void setValueWithExpireTime(String key, Object value, long timeout, TimeUnit unit) {
        redisTemplate.opsForValue().set(key, value, timeout, unit);
    }

    /**
     * Redis에서 키에 해당하는 값을 삭제.
     *
     * @param key Redis에서 삭제할 키.
     */
    public void deleteValue(String key) {
        redisTemplate.delete(key);
    }

    /**
     * // 주어진 key와 hashKey에 해당하는 값을 Redis에서 삭제한다.
     * @param key   Redis에서 삭제할 키.
     * @param hashKey   Redis에서 삭제할 해쉬 키.
     */
    public void deleteHashValue(String key, String hashKey) {
        hashOperations.delete(key, hashKey);
    }
}

