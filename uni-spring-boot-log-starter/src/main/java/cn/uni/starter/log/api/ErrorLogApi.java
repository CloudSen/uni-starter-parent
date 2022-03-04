package cn.uni.starter.log.api;

import cn.uni.starter.log.dto.UniLogErrorDTO;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author <bailong>
 * @date 2022-03-04
 */
public interface ErrorLogApi {

    String API_PREFIX = "/log/api/log-error";

    /**
     * 保存错误日志
     *
     * @param uniLogErrorDTO 错误日志信息
     * @return 结果
     */
    @PostMapping(API_PREFIX + "/saveErrorLog")
    void saveErrorLog(@RequestBody UniLogErrorDTO uniLogErrorDTO);
}
