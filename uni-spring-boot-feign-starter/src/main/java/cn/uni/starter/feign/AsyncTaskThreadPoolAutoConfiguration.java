package cn.uni.starter.feign;

import cn.uni.starter.feign.properties.AsyncTaskExecutorProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * @author CloudS3n
 * @date 2021-07-26 14:15
 */
@Log4j2
@Configuration
@RequiredArgsConstructor
@SuppressWarnings("SpringFacetCodeInspection")
@EnableConfigurationProperties({AsyncTaskExecutorProperties.class})
@ConditionalOnProperty(prefix = "uni.autoconfigure", name = "enable-thread-pool", havingValue = "true")
public class AsyncTaskThreadPoolAutoConfiguration {

    private final AsyncTaskExecutorProperties properties;

    @Bean
    @ConditionalOnMissingBean(name = "asyncTaskExecutor")
    public ThreadPoolTaskExecutor asyncTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(properties.getCorePoolSize());
        executor.setMaxPoolSize(properties.getMaxPoolSize());
        executor.setQueueCapacity(properties.getQueueCapacity());
        executor.setThreadNamePrefix(properties.getThreadNamePrefix());
        executor.setAllowCoreThreadTimeOut(properties.getAllowCoreThreadTimeout());
        executor.setKeepAliveSeconds((int) properties.getKeepAliveSeconds().getSeconds());
        executor.setTaskDecorator(new AsyncTaskDecorator());
        executor.initialize();
        return executor;
    }
}
