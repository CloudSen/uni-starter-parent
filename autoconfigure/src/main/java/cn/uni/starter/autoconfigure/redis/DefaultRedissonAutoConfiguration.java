package cn.uni.starter.autoconfigure.redis;

import cn.uni.starter.autoconfigure.AutoConfigConstants;
import cn.uni.starter.autoconfigure.redis.properties.UniRedissonProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
@RequiredArgsConstructor
@Configuration
@EnableConfigurationProperties(UniRedissonProperties.class)
@ConditionalOnBean({RedisOperations.class, RedissonClient.class})
@ConditionalOnProperty(name = "uni.autoconfigure.enable-redisson-cache-manager", havingValue = "true")
@AutoConfigureAfter(RedissonAutoConfiguration.class)
@SuppressWarnings({"RedundantThrows", "SpringJavaInjectionPointsAutowiringInspection"})
public class DefaultRedissonAutoConfiguration {

    static {
        log.info(AutoConfigConstants.LOADING_REDISSON_AUTO_CONFIGURE);
    }

    private final UniRedissonProperties uniRedissonProperties;

    @Bean
    public CacheManager cacheManager(RedissonClient redissonClient) throws IOException {
        log.info(AutoConfigConstants.LOADING_REDISSON_CACHE_MGMT_AUTO_CONFIGURE);
        Map<String, CacheConfig> cacheConfigMap = new HashMap<>(16);
        Map<String, UniRedissonProperties.RedissonCacheManagerExpireTime> cacheManagerExpireTimeMap =
            uniRedissonProperties.getCacheManagerExpireTimeMap();
        if (MapUtils.isNotEmpty(cacheManagerExpireTimeMap)) {
            cacheManagerExpireTimeMap.forEach((k, v) -> cacheConfigMap.put(k, new CacheConfig(v.getTtl().toMillis(), v.getMaxIdleTime().toMillis())));
        }
        return new RedissonSpringCacheManager(redissonClient, cacheConfigMap);
    }
}
