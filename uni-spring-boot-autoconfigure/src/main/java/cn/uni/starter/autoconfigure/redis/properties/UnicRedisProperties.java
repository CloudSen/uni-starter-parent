package cn.uni.starter.autoconfigure.redis.properties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.time.Duration;
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
@ConfigurationProperties(prefix = "uni.config.redis")
public class UnicRedisProperties {

    /**
     * <p>自定义不同的cache manager过期时间</p>
     * <p>KEY: cache manager名字</p>
     * <p>VALUE: 过期时间Duration</p>
     */
    private Map<String, Duration> expires = new HashMap<>(16);
}
