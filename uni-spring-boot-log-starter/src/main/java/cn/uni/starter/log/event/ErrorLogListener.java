package cn.uni.starter.log.event;

import cn.uni.starter.log.constant.EventConstant;
import cn.uni.starter.log.dto.UniLogErrorDTO;
import cn.uni.starter.log.feign.UniLogErrorFeign;
import cn.uni.starter.log.server.ServerInfo;
import cn.uni.starter.log.utils.LogAbstractUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.Async;

import java.util.Map;

/**
 * 异步监听错误日志事件
 *
 * @author <bailong>
 * @date 2022-03-02
 */
@RequiredArgsConstructor
public class ErrorLogListener {

    private final ServerInfo serviceInfo;
    private final UniLogErrorFeign uniLogErrorFeign;

    /**
     * 异步监听错误日志事件
     *
     * @param event 错误日志事件
     */
    @SuppressWarnings("unchecked")
    @Async("commonTaskExecutor")
    @Order
    @EventListener(ErrorLogEvent.class)
    public void saveErrorLog(ErrorLogEvent event) {
        Map<String, Object> source = (Map<String, Object>) event.getSource();
        UniLogErrorDTO logErrorDTO = (UniLogErrorDTO) source.get(EventConstant.EVENT_LOG);
        logErrorDTO.setServiceIp(serviceInfo.getIpWithPort());
        //加入其他日志信息
        LogAbstractUtil.addOtherInfoToLog(logErrorDTO, serviceInfo);
        uniLogErrorFeign.saveErrorLog(logErrorDTO);
    }
}
