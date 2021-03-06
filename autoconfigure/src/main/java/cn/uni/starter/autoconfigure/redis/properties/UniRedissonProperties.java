package cn.uni.starter.autoconfigure.redis.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Map;

/**
 * @author CloudS3n
 * @date 2021-06-16 19:01
 */
@Data
@Component
@ConfigurationProperties(prefix = "uni.config.redisson")
public class UniRedissonProperties {

    private final Map<String, RedissonCacheManagerExpireTime> cacheManagerExpireTimeMap;

    @Data
    public static class RedissonCacheManagerExpireTime {
        private Duration ttl;
        private Duration maxIdleTime;
    }
}
