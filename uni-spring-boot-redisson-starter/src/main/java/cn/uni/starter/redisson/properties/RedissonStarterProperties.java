package cn.uni.starter.redisson.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

/**
 * @author CloudS3n
 * @date 2021-06-17 09:18
 */
@Data
@Validated
@Component
@ConfigurationProperties(prefix = "uni.autoconfigure")
public class RedissonStarterProperties {

    @NotNull
    private Boolean enableRedissonCacheManager;
}
