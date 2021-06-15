package cn.unic.starter.autoconfigure;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
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
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@Component
@ConfigurationProperties(prefix = "unic.config.starter", ignoreUnknownFields = false)
public class UnicStarterProperties {

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
    private Boolean enableRedis = true;

    @NotNull
    private Boolean enableSecurity = true;

    @NotNull
    private Boolean enableOauth = true;
}
