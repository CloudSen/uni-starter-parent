package cn.uni.starter.log.publisher;

import cn.uni.starter.log.annotation.ApiLog;
import cn.uni.starter.log.constant.EventConstant;
import cn.uni.starter.log.dto.UniLogApiDTO;
import cn.uni.starter.log.event.ApiLogEvent;
import cn.uni.starter.log.filter.ReHttpServletRequestWrapper;
import cn.uni.starter.log.utils.LogAbstractUtil;
import cn.uni.starter.log.utils.SpringEventUtil;
import cn.uni.starter.log.utils.WebUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * API日志信息事件发送
 *
 * @author <bailong>
 * @date 2022-03-03
 */
public class ApiLogPublisher {

    /**
     * 默认日志类型
     */
    private static final String LOG_NORMAL_TYPE = "1";

    /**
     * API日志事件推送
     *
     * @param methodName  方法名
     * @param methodClass 方法类
     * @param apiLog      api日志类
     * @param time        执行时间
     */
    public static void publishEvent(String methodName, String methodClass, ApiLog apiLog, long time) {
        ReHttpServletRequestWrapper request = WebUtil.getRequest();
        UniLogApiDTO logApi = new UniLogApiDTO();
        logApi.setType(LOG_NORMAL_TYPE);
        logApi.setTitle(apiLog.value());
        logApi.setTime(time + " ms");
        logApi.setMethodClass(methodClass);
        logApi.setMethodName(methodName);
        LogAbstractUtil.addRequestInfoToLog(request, logApi);
        Map<String, Object> event = new HashMap<>(16);
        event.put(EventConstant.EVENT_LOG, logApi);
        SpringEventUtil.publishEvent(new ApiLogEvent(event));
    }
}
