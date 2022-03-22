package cn.uni.starter.log.publisher;


import cn.uni.starter.log.constant.EventConstant;
import cn.uni.starter.log.dto.UniLogErrorDTO;
import cn.uni.starter.log.event.ErrorLogEvent;
import cn.uni.starter.log.filter.ReHttpServletRequestWrapper;
import cn.uni.starter.log.utils.LogAbstractUtil;
import cn.uni.starter.log.utils.SpringEventUtil;
import cn.uni.starter.log.utils.WebUtil;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.util.ObjectUtils;

import java.util.HashMap;
import java.util.Map;


/**
 * 异常信息事件发送
 *
 * @author <bailong>
 * @date 2022-03-02
 */
public class ErrorLogPublisher {

    /**
     * 发送异常事件，设定相应的信息
     *
     * @param error      异常信息
     * @param requestUrl 请求信息
     */
    public static void publishEvent(Throwable error, String requestUrl) {
        ReHttpServletRequestWrapper request = WebUtil.getRequest();
        UniLogErrorDTO uniLogErrorDTO = new UniLogErrorDTO();
        uniLogErrorDTO.setRequestUri(requestUrl);
        if (!ObjectUtils.isEmpty(error)) {
            //异常信息
            uniLogErrorDTO.setStackTrace(ExceptionUtils.getStackTrace(error));
            uniLogErrorDTO.setExceptionName(error.getClass().getName());
            uniLogErrorDTO.setMessage(error.getMessage());
            StackTraceElement[] elements = error.getStackTrace();
            if (!ObjectUtils.isEmpty(elements)) {
                //具体堆栈信息
                StackTraceElement element = elements[0];
                uniLogErrorDTO.setMethodName(element.getMethodName());
                uniLogErrorDTO.setMethodClass(element.getClassName());
                uniLogErrorDTO.setFileName(element.getFileName());
                uniLogErrorDTO.setLineNumber(element.getLineNumber());
            }
        }
        //注入request信息
        LogAbstractUtil.addRequestInfoToLog(request, uniLogErrorDTO);
        Map<String, Object> event = new HashMap<>(16);
        event.put(EventConstant.EVENT_LOG, uniLogErrorDTO);
        //发布事件
        SpringEventUtil.publishEvent(new ErrorLogEvent(event));
    }
}
