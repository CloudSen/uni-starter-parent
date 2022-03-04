package cn.uni.starter.log.config;

import cn.uni.starter.log.event.ApiLogListener;
import cn.uni.starter.log.event.ErrorLogListener;
import cn.uni.starter.log.feign.UniLogApiFeign;
import cn.uni.starter.log.feign.UniLogErrorFeign;
import cn.uni.starter.log.properties.RequestLogProperties;
import cn.uni.starter.log.server.ServerInfo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 自动装配
 *
 * @author <bailong>
 * @date 2022-03-03
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnWebApplication
@EnableConfigurationProperties(RequestLogProperties.class)
public class LogToolAutoConfig {

    public static String appName;
    public static String env;

    @Value("${spring.profiles.active}")
    public void setEnv(String env) {
        LogToolAutoConfig.env = env;
    }

    @Value("${spring.application.name}")
    public void setAppName(String appName) {
        LogToolAutoConfig.appName = appName;
    }

    @Bean
    @ConditionalOnMissingBean(name = "apiLogListener")
    public ApiLogListener apiLogListener(UniLogApiFeign apiFeign, ServerInfo serverInfo) {
        return new ApiLogListener(serverInfo, apiFeign);
    }

    @Bean
    @ConditionalOnMissingBean(name = "errorEventListener")
    public ErrorLogListener errorEventListener(UniLogErrorFeign errorFeign, ServerInfo serverInfo) {
        return new ErrorLogListener(serverInfo, errorFeign);
    }

}
