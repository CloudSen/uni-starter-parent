package cn.uni.starter.autoconfigure.mvc.intercepter;

import cn.hutool.core.date.LocalDateTimeUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.NamedThreadLocal;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Enumeration;

/**
 * 请求日志拦截器
 *
 * @author CloudS3n
 * @date 2021-06-15 11:42
 */
@Slf4j
@Component
@SuppressWarnings({"NullableProblems", "RedundantThrows"})
public class DefaultLoggingInterceptor implements HandlerInterceptor {

    private static final ThreadLocal<LocalDateTime> REQUEST_TIME = new NamedThreadLocal<>("Request Duration ThreadLocal");

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        REQUEST_TIME.set(LocalDateTime.now());
        logRequest(request);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        log.info("Response: [{}]", response.getStatus());
        LocalDateTime before = REQUEST_TIME.get();
        log.info("request costs(ms):{}", LocalDateTimeUtil.between(before, LocalDateTime.now()).toMillis());
        REQUEST_TIME.remove();
    }

    private void logRequest(HttpServletRequest request) {
        String queryString = request.getQueryString();
        if (HttpMethod.GET.name().equalsIgnoreCase(request.getMethod()) && !StringUtils.isBlank(queryString)) {
            queryString = URLDecoder.decode(queryString, StandardCharsets.UTF_8);
        }
        StringBuilder headers = new StringBuilder();
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            headers.append(headerName).append(":").append(request.getHeader(headerName)).append("\n");
        }
        log.info("Request: [{}] [{}] [{}]\nRequest header: {}",
            request.getMethod(), request.getRequestURL(), queryString, headers);
    }
}
