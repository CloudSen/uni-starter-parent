package cn.uni.starter.feign;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

/**
 * Feign调用请求头参数设置
 *
 * @author younikong
 */
@Log4j2
@Configuration
@SuppressWarnings("SpringFacetCodeInspection")
public class FeignConfiguration implements RequestInterceptor {
    private static final String AUTHORIZATION = "Authorization";

    @Override
    public void apply(RequestTemplate requestTemplate) {
        ServletRequestAttributes attributes = (ServletRequestAttributes)
            RequestContextHolder.getRequestAttributes();
        if (Objects.isNull(attributes)) {
            log.warn("Feign请求未获取到Header参数, 请注意上下文是否正确传递！");
            return;
        }
        HttpServletRequest request = attributes.getRequest();
        if (StringUtils.isNotBlank(request.getHeader(AUTHORIZATION))) {
            requestTemplate.header(AUTHORIZATION, request.getHeader(AUTHORIZATION));
        }
    }
}
