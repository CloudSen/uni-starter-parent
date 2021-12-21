package cn.uni.starter.autoconfigure.mvc.intercepter;

import cn.uni.starter.autoconfigure.context.AsyncRequestHolder;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Enumeration;
import java.util.Map;

/**
 * 异步请求拦截，设置ttl请求头
 *
 * @author clouds3n
 * @since 2021-12-20
 */
@Log4j2
@Component
@SuppressWarnings("NullableProblems")
public class DefaultAsyncHeaderInterceptor implements HandlerInterceptor {

    private static final String AUTHORIZATION = "authorization";
    private static final String ASYNC = "async";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String uri = request.getRequestURI();
        if (StringUtils.isBlank(uri) || !StringUtils.contains(uri, ASYNC)) {
            return true;
        }
        // 把异步请求的请求头设置到ttl中
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String name = headerNames.nextElement();
            if (StringUtils.equals(name, AUTHORIZATION)) {
                log.info("> AsyncRequestHolder add {}-{}", name, request.getHeader(name));
                AsyncRequestHolder.TTL_THREAD_LOCAL.get().put(name, request.getHeader(name));
            } else {
                log.info("> AsyncRequestHolder ignore {}-{}", name, request.getHeader(name));
            }
        }
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        AsyncRequestHolder.TTL_THREAD_LOCAL.remove();
        Map<String, String> map = AsyncRequestHolder.TTL_THREAD_LOCAL.get();
        log.info("> AsyncRequestHolder removed, current size={}", MapUtils.isEmpty(map) ? 0 : map.size());
    }
}
