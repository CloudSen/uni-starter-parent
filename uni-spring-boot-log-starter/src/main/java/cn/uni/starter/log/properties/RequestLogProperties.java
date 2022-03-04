package cn.uni.starter.log.properties;

import cn.uni.starter.log.constant.LogConstant;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

/**
 * @author <bailong>
 * @date 2022-03-03
 */
@Getter
@Setter
@RefreshScope
@ConfigurationProperties(LogConstant.LOG_PROPS_PREFIX)
@Component
public class RequestLogProperties {

    private Boolean enabled = true;
}
