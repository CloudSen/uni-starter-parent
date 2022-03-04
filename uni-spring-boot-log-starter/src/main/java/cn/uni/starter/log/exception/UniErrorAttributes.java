package cn.uni.starter.log.exception;

import cn.uni.starter.log.properties.UniLogEnableProperties;
import cn.uni.starter.log.publisher.ErrorLogPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.lang.Nullable;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.WebRequest;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * 请求异常，异数获取
 *
 * @author <bailong>
 * @date 2022-03-03
 */
@RequiredArgsConstructor
@Log4j2
public class UniErrorAttributes extends DefaultErrorAttributes {

    private final UniLogEnableProperties uniLogEnableProperties;

    @Override
    public Map<String, Object> getErrorAttributes(WebRequest webRequest, ErrorAttributeOptions options) {
        if (!uniLogEnableProperties.isErrorLogEnabled()) {
            log.info("异步写入error日志配置为关闭状态");
            return Collections.emptyMap();
        }

        String requestUri = this.getAttr(webRequest, "javax.servlet.error.request_uri");
        Throwable error = getError(webRequest);

        //发送服务异常事件
        ErrorLogPublisher.publishEvent(error, requestUri);
        return new HashMap<>(0);
    }

    @Nullable
    private String getAttr(WebRequest webRequest, String name) {
        return String.valueOf(webRequest.getAttribute(name, RequestAttributes.SCOPE_REQUEST));
    }
}
