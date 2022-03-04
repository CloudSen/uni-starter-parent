package cn.uni.starter.log.feign;

import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

/**
 * error日志feign回调类
 *
 * @author <bailong>
 * @date 2022-03-03
 */
@Log4j2
@Component
public class UniLogErrorFallback implements FallbackFactory<UniLogErrorFeign> {
    @Override
    public UniLogErrorFeign create(Throwable cause) {
        log.error("[错误日志保存接口] 异常:\n{}", ExceptionUtils.getStackTrace(cause));
        return log::error;
    }
}
