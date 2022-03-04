package cn.uni.starter.log.api;

import cn.uni.starter.log.dto.UniLogApiDTO;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * Api日志类
 *
 * @author <bailong>
 * @date 2022-03-04
 */
public interface LogApi {

    String API_PREFIX = "/log/api/log-api";

    /**
     * 保存Api日志
     *
     * @param uniLogApiDTO Api日志信息
     * @return 结果
     */
    @PostMapping(API_PREFIX + "/saveApiLog")
    void saveApiLog(@RequestBody UniLogApiDTO uniLogApiDTO);
}
