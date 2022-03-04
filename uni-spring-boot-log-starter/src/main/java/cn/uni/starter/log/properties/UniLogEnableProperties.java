package cn.uni.starter.log.properties;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 日志开启信息
 *
 * @author <bailong>
 * @date 2022-03-04
 */
@Data
@Accessors(chain = true)
@Component
@ConfigurationProperties(prefix = "uni.log")
public class UniLogEnableProperties {

    /**
     * 是否开启异常日志写入
     */
    private boolean errorLogEnabled;

    /**
     * 是否开启api日志写入
     */
    private boolean apiLogEnabled;
}
