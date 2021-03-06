package cn.uni.starter.autoconfigure.redis;

import cn.uni.starter.autoconfigure.AutoConfigConstants;
import cn.uni.starter.autoconfigure.redis.properties.UniRedisProperties;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * 老系统的Redis配置（无Redisson）
 *
 * @author CloudS3n
 * @date 2021-06-11 12:01
 */
@Slf4j
@RequiredArgsConstructor
@Configuration
@EnableConfigurationProperties(UniRedisProperties.class)
@ConditionalOnClass(RedisOperations.class)
@ConditionalOnMissingClass({"org.redisson.spring.starter.RedissonAutoConfiguration"})
@ConditionalOnProperty(name = "uni.autoconfigure.enable-old-redis", havingValue = "true")
@AutoConfigureBefore(RedisAutoConfiguration.class)
public class DefaultOldRedisAutoConfiguration {

    static {
        log.info(AutoConfigConstants.LOADING_OLD_REDIS_AUTO_CONFIGURE);
    }

    private final UniRedisProperties uniRedisProperties;

    @Bean
    @ConditionalOnMissingBean(name = "redisTemplate")
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        log.info(AutoConfigConstants.LOADING_OLD_REDIS_TEMPLATE);
        RedisSerializer<Object> serializer = redisSerializer();
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(serializer);
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashValueSerializer(serializer);
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }

    @Bean
    @ConditionalOnMissingBean(name = "redisSerializer")
    public RedisSerializer<Object> redisSerializer() {
        log.info(AutoConfigConstants.LOADING_OLD_REDIS_SERIALIZER);
        Jackson2JsonRedisSerializer<Object> serializer = new Jackson2JsonRedisSerializer<>(Object.class);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        objectMapper.activateDefaultTyping(LaissezFaireSubTypeValidator.instance, ObjectMapper.DefaultTyping.NON_FINAL);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        serializer.setObjectMapper(objectMapper);
        return serializer;
    }

    @Bean
    @ConditionalOnMissingBean(name = "redisCacheManager")
    public RedisCacheManager redisCacheManager(RedisConnectionFactory redisConnectionFactory) {
        log.info(AutoConfigConstants.LOADING_OLD_REDIS_CACHE);
        //设置Redis缓存有效期为1天
        RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
            .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(redisSerializer()))
            .entryTtl(Duration.ofMillis(10));
        Set<String> cacheNames = new HashSet<>();
        Map<String, RedisCacheConfiguration> configMap = new HashMap<>(16);
        uniRedisProperties.getExpires()
            .forEach((k, d) -> {
                cacheNames.add(k);
                configMap.put(k, redisCacheConfiguration.entryTtl(d));
            });
        return RedisCacheManager.builder(redisConnectionFactory)
            .cacheDefaults(redisCacheConfiguration)
            .initialCacheNames(cacheNames)
            .withInitialCacheConfigurations(configMap)
            .build();
    }
}
