package CoinIndicator.CoinIndicator.redis;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;

@SpringBootTest
public class RedisTest {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Test
    void testRedisConnection() {
        stringRedisTemplate.opsForValue().set("testkey", "value");
        String value = stringRedisTemplate.opsForValue().get("testkey");
        System.out.println("Value from Redis: " + value);

        Assertions.assertThat(value).isEqualTo("value");
    }
}
