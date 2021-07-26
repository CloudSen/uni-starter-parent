package cn.uni.starter.feign.properties;

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
 * 异步任务线程池配置参数
 *
 * @author CloudS3n
 * @date 2021-07-26 14:16
 */
@Validated
@Data
@Accessors(chain = true)
@Component
@ConfigurationProperties(prefix = "uni.config.thread-pool.async-executor", ignoreUnknownFields = false)
public class AsyncTaskExecutorProperties {

    @NotBlank
    private String threadNamePrefix = "async-executor-";

    @Range(min = 10, max = Integer.MAX_VALUE)
    private Integer corePoolSize = 15;

    @Range(min = 50, max = Integer.MAX_VALUE)
    private Integer maxPoolSize = 50;

    @Range(min = 0, max = Integer.MAX_VALUE)
    private Integer queueCapacity = 200;

    @NotNull
    private Boolean allowCoreThreadTimeout = false;

    @NotNull
    private Duration keepAliveSeconds = Duration.ofSeconds(30);
}
