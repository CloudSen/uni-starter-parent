package cn.unic.starter.autoconfigure.redis;

import cn.unic.starter.autoconfigure.redis.properties.UnicRedissonProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.collections4.MapUtils;
import org.redisson.api.RedissonClient;
import org.redisson.spring.cache.CacheConfig;
import org.redisson.spring.cache.RedissonSpringCacheManager;
import org.redisson.spring.starter.RedissonAutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisOperations;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Redisson客户端的Spring Cache配置
 *
 * @author CloudS3n
 * @date 2021-06-16 13:31
 */
@Log4j2
@RequiredArgsConstructor
@Configuration
@EnableConfigurationProperties(UnicRedissonProperties.class)
@ConditionalOnBean({RedisOperations.class, RedissonClient.class})
@ConditionalOnProperty(name = "unic.config.default.enable-redisson-cache-manager", havingValue = "true")
@AutoConfigureAfter(RedissonAutoConfiguration.class)
@SuppressWarnings("RedundantThrows")
public class DefaultRedissonAutoConfiguration {

    private final UnicRedissonProperties unicRedissonProperties;

    @Bean
    @ConditionalOnProperty(name = "unic.config.redisson.cache-manager-expire-time-map")
    public CacheManager cacheManager(RedissonClient redissonClient) throws IOException {
        Map<String, CacheConfig> cacheConfigMap = new HashMap<>(16);
        Map<String, UnicRedissonProperties.RedissonCacheManagerExpireTime> cacheManagerExpireTimeMap =
            unicRedissonProperties.getCacheManagerExpireTimeMap();
        if (MapUtils.isNotEmpty(cacheManagerExpireTimeMap)) {
            cacheManagerExpireTimeMap.forEach((k, v) -> cacheConfigMap.put(k, new CacheConfig(v.getTtl().toMillis(), v.getMaxIdleTime().toMillis())));
        }
        return new RedissonSpringCacheManager(redissonClient, cacheConfigMap);
    }
}
