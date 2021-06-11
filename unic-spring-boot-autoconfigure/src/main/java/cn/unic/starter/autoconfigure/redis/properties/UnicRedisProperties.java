package cn.unic.starter.autoconfigure.redis.properties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author CloudS3n
 * @date 2021-06-11 11:59
 */
@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@Component
@ConfigurationProperties(prefix = "unic.config.redis")
public class UnicRedisProperties {

    /**
     * 过期时间
     */
    private Map<String, Long> expires = new HashMap<>();
}
