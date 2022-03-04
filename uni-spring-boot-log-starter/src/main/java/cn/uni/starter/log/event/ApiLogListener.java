package cn.uni.starter.log.event;

import cn.uni.starter.log.constant.EventConstant;
import cn.uni.starter.log.dto.UniLogApiDTO;
import cn.uni.starter.log.feign.UniLogApiFeign;
import cn.uni.starter.log.server.ServerInfo;
import cn.uni.starter.log.utils.LogAbstractUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.Async;

import java.util.Map;

/**
 * Api日志异步监听事件
 *
 * @author <bailong>
 * @date 2022-03-03
 */
@RequiredArgsConstructor
public class ApiLogListener {

    private final ServerInfo serviceInfo;
    private final UniLogApiFeign uniLogApiFeign;

    /**
     * 异步监听Api调用事件
     *
     * @param event api日志事件
     */
    @SuppressWarnings("unchecked")
    @Async("commonTaskExecutor")
    @Order
    @EventListener(ApiLogEvent.class)
    public void saveErrorLog(ApiLogEvent event) {
        Map<String, Object> source = (Map<String, Object>) event.getSource();
        UniLogApiDTO logApi = (UniLogApiDTO) source.get(EventConstant.EVENT_LOG);
        //加入其他日志信息
        LogAbstractUtil.addOtherInfoToLog(logApi, serviceInfo);
        uniLogApiFeign.saveApiLog(logApi);
    }
}
