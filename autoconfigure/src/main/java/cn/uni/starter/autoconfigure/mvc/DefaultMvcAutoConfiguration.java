package cn.uni.starter.autoconfigure.mvc;

import cn.uni.starter.autoconfigure.AutoConfigConstants;
import cn.uni.starter.autoconfigure.mvc.intercepter.DefaultLoggingInterceptor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author CloudS3n
 * @date 2021-06-15 12:06
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
@SuppressWarnings("NullableProblems")
@ConditionalOnProperty(name = "uni.autoconfigure.enable-mvc", havingValue = "true")
public class DefaultMvcAutoConfiguration implements WebMvcConfigurer {

    static {
        log.info(AutoConfigConstants.LOADING_MVC_AUTO_CONFIGURE);
    }

    private final DefaultLoggingInterceptor defaultLoggingInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        WebMvcConfigurer.super.addInterceptors(registry);
        registry.addInterceptor(defaultLoggingInterceptor)
            .excludePathPatterns("/*/*.html",
                "/*/*.css",
                "/*/*.js",
                "/*/*.png",
                "/swagger*/**");
    }
}
