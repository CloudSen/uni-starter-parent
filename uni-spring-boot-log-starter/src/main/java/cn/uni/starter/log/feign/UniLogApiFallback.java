package cn.uni.starter.log.feign;

import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

/**
 * api日志feign回调类
 *
 * @author <bailong>
 * @date 2022-03-03
 */
@Log4j2
@Component
public class UniLogApiFallback implements FallbackFactory<UniLogApiFeign> {
    @Override
    public UniLogApiFeign create(Throwable cause) {
        log.error("[api日志保存接口] 异常:\n{}", ExceptionUtils.getStackTrace(cause));
        return log::error;
    }
}
