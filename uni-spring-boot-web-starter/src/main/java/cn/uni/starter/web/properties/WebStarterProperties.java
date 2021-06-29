package cn.uni.starter.web.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

/**
 * 默认配置开关
 *
 * @author CloudS3n
 * @date 2021-06-15 14:12
 */
@Data
@Validated
@Component
@ConfigurationProperties(prefix = "uni.autoconfigure")
public class WebStarterProperties {

    @NotNull
    private Boolean enableMvc = true;

    @NotNull
    private Boolean enableExceptionHandler = true;

    @NotNull
    private Boolean enableThreadPool = true;

    @NotNull
    private Boolean enableSerializer = true;
}
