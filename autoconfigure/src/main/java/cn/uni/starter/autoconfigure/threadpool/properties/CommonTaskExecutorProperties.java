package cn.uni.starter.autoconfigure.threadpool.properties;

import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Range;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.Duration;

/**
 *
 * @author CloudS3n
 * @date 2021-06-11 10:11
 */
@Validated
@Data
@Accessors(chain = true)
@Component
@ConfigurationProperties(prefix = "uni.config.thread-pool.executor")
public class CommonTaskExecutorProperties {

    @NotBlank
    private String threadNamePrefix = "common-executor-";

    @Range(min = 1, max = Integer.MAX_VALUE)
    private Integer corePoolSize = 5;

    @Range(min = 1, max = Integer.MAX_VALUE)
    private Integer maxPoolSize = 20;

    @Range(min = 0, max = Integer.MAX_VALUE)
    private Integer queueCapacity = 30;

    @NotNull
    private Boolean allowCoreThreadTimeout = false;

    @NotNull
    private Duration keepAliveSeconds = Duration.ofSeconds(30);
}
