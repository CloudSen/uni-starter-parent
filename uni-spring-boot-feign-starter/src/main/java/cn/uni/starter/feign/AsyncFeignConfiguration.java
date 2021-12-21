package cn.uni.starter.feign;

import cn.uni.starter.autoconfigure.context.AsyncRequestHolder;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

/**
 * @author clouds3n
 * @since 2021-12-20
 */
@Log4j2
@Configuration
public class AsyncFeignConfiguration implements RequestInterceptor {

    private static final String AUTHORIZATION = "authorization";

    @Override
    public void apply(RequestTemplate requestTemplate) {
        Map<String, String> headerMap = AsyncRequestHolder.get();
        if (MapUtils.isEmpty(headerMap) || StringUtils.isBlank(headerMap.get(AUTHORIZATION))) {
            log.warn("Async Feign请求未获取到Header Authorization参数, 请注意上下文是否正确传递！");
            return;
        }
        requestTemplate.header(AUTHORIZATION, headerMap.get(AUTHORIZATION));
    }
}
