package cn.uni.starter.autoconfigure.mvc.intercepter;

import cn.hutool.core.date.LocalDateTimeUtil;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.NamedThreadLocal;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;

/**
 * 请求日志拦截器
 *
 * @author CloudS3n
 * @date 2021-06-15 11:42
 */
@Log4j2
@Component
@SuppressWarnings({"NullableProblems", "RedundantThrows"})
public class DefaultLoggingInterceptor implements HandlerInterceptor {

    private static final ThreadLocal<LocalDateTime> REQUEST_TIME = new NamedThreadLocal<>("Request Duration ThreadLocal");

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        REQUEST_TIME.set(LocalDateTime.now());
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        log.info("Response: [{}]", response.getStatus());
        LocalDateTime before = REQUEST_TIME.get();
        log.info("request costs(ms):{}", LocalDateTimeUtil.between(before, LocalDateTime.now()).toMillis());
        REQUEST_TIME.remove();
    }
}
