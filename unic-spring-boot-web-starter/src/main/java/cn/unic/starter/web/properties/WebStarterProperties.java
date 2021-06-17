package cn.unic.starter.web.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;

/**
 * Starter启用参数
 *
 * @author CloudS3n
 * @date 2021-06-15 14:12
 */
@Data
@Component
@ConfigurationProperties(prefix = "unic.config.starter", ignoreUnknownFields = false)
public class WebStarterProperties {

    @NotNull
    private Boolean enableMvc = true;

    @NotNull
    private Boolean enableExceptionHandler = true;

    @NotNull
    private Boolean enableMybatisPlus = true;

    @NotNull
    private Boolean enableEs = true;

    @NotNull
    private Boolean enableThreadPool = true;

    @NotNull
    private Boolean enableSecurity = true;

    @NotNull
    private Boolean enableOauth = true;
}
