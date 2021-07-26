package cn.uni.starter.autoconfigure.threadpool.properties;

import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Range;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;

/**
 * @author CloudS3n
 * @date 2021-06-11 10:11
 */
@Validated
@Data
@Accessors(chain = true)
@Component
@ConfigurationProperties(prefix = "uni.config.thread-pool.scheduler", ignoreUnknownFields = false)
public class CommonTaskSchedulerProperties {

    @NotBlank
    private String threadNamePrefix = "common-scheduler-";

    @Range(min = 10, max = Integer.MAX_VALUE)
    private Integer poolSize = 10;
}
