package cn.unic.starter.redis.properties;

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
@ConfigurationProperties(prefix = "unic.config.default", ignoreUnknownFields = false)
public class RedisStarterProperties {

    @NotNull
    private Boolean enableOldRedis = true;
}
