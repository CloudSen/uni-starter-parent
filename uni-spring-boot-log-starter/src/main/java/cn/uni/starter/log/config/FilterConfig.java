package cn.uni.starter.log.config;

import cn.uni.starter.log.filter.StreamFilter;
import lombok.AllArgsConstructor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 过滤器配置
 *
 * @author <bailong>
 * @date 2022-03-22
 */
@Configuration
@AllArgsConstructor
public class FilterConfig {

    private final StreamFilter streamFilter;

    @Bean
    public FilterRegistrationBean registerFilter() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(streamFilter);
        return registration;
    }
}

