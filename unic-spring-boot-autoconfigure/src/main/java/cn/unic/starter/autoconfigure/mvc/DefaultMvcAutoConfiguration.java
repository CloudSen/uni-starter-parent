package cn.unic.starter.autoconfigure.mvc;

import cn.unic.starter.autoconfigure.mvc.intercepter.DefaultLoggingInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author CloudS3n
 * @date 2021-06-15 12:06
 */
@Configuration
@RequiredArgsConstructor
@SuppressWarnings("NullableProblems")
@ConditionalOnProperty(name = "unic.config.default.enable-mvc", havingValue = "true")
public class DefaultMvcAutoConfiguration implements WebMvcConfigurer {

    private final DefaultLoggingInterceptor defaultLoggingInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        WebMvcConfigurer.super.addInterceptors(registry);
        registry.addInterceptor(defaultLoggingInterceptor)
            .excludePathPatterns("/**/*.html",
                "/**/*.css",
                "/**/*.js",
                "/**/*.png",
                "/swagger*/**");
    }
}
