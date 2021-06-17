package cn.unic.starter.redisson.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;

/**
 * @author CloudS3n
 * @date 2021-06-17 09:18
 */
@Data
@Component
@ConfigurationProperties(prefix = "unic.config.starter", ignoreUnknownFields = false)
public class RedissonStarterProperties {
    
    @NotNull
    private Boolean enableRedissonCacheManager;
}
