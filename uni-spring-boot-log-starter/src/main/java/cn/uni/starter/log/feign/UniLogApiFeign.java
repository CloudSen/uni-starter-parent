package cn.uni.starter.log.feign;

import cn.uni.starter.log.api.LogApi;
import cn.uni.starter.log.constant.LogConstant;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * API日志feign接口类
 *
 * @author <bailong>
 * @date 2022-03-03
 */
@FeignClient(
    name = LogConstant.UNI_LOG_NAME,
    contextId = "apiLog",
    fallbackFactory = UniLogErrorFallback.class
)
public interface UniLogApiFeign extends LogApi {

}
