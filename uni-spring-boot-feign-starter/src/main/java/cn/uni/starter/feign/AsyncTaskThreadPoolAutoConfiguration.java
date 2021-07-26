package cn.uni.starter.feign;

import cn.uni.starter.feign.properties.AsyncTaskExecutorProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.openfeign.FeignClient;
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
@EnableConfigurationProperties({AsyncTaskExecutorProperties.class})
@ConditionalOnProperty(prefix = "uni.autoconfigure", name = "enable-thread-pool", havingValue = "true")
@ConditionalOnClass(FeignClient.class)
public class AsyncTaskThreadPoolAutoConfiguration {

    private final AsyncTaskExecutorProperties asyncTaskExecutorProperties;

    static {
        log.info("[ 自动装配 ] 加载异步任务线程池配置...");
    }

    @Bean
    @ConditionalOnMissingBean(name = "asyncTaskExecutor")
    public ThreadPoolTaskExecutor asyncTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(asyncTaskExecutorProperties.getCorePoolSize());
        executor.setMaxPoolSize(asyncTaskExecutorProperties.getMaxPoolSize());
        executor.setQueueCapacity(asyncTaskExecutorProperties.getQueueCapacity());
        executor.setThreadNamePrefix(asyncTaskExecutorProperties.getThreadNamePrefix());
        executor.setAllowCoreThreadTimeOut(asyncTaskExecutorProperties.getAllowCoreThreadTimeout());
        executor.setKeepAliveSeconds((int) asyncTaskExecutorProperties.getKeepAliveSeconds().getSeconds());
        executor.setTaskDecorator(new AsyncTaskDecorator());
        executor.initialize();
        return executor;
    }
}
