package cn.uni.starter.log.feign;

import cn.uni.starter.log.api.ErrorLogApi;
import cn.uni.starter.log.constant.LogConstant;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * 错误日志Feign接口类
 *
 * @author <bailong>
 * @date 2022-03-03
 */
@FeignClient(
    name = LogConstant.UNI_LOG_NAME,
    contextId = "errorLog",
    fallbackFactory = UniLogErrorFallback.class
)
public interface UniLogErrorFeign extends ErrorLogApi {

}
