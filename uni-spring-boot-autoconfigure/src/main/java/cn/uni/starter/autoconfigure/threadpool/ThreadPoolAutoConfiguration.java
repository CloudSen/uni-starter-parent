package cn.uni.starter.autoconfigure.threadpool;

import cn.uni.starter.autoconfigure.AutoConfigConstants;
import cn.uni.starter.autoconfigure.threadpool.properties.CommonTaskExecutorProperties;
import cn.uni.starter.autoconfigure.threadpool.properties.CommonTaskSchedulerProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

/**
 * @author CloudS3n
 * @date 2021-06-11 10:11
 */
@Log4j2
@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties({CommonTaskExecutorProperties.class, CommonTaskSchedulerProperties.class})
@ConditionalOnProperty(prefix = "unic.autoconfigure", name = "enable-thread-pool", havingValue = "true")
public class ThreadPoolAutoConfiguration {

    private final CommonTaskExecutorProperties commonTaskExecutorProperties;
    private final CommonTaskSchedulerProperties commonTaskSchedulerProperties;

    static {
        log.info(AutoConfigConstants.LOADING_THREAD_POOL_AUTO_CONFIGURE);
    }

    @Bean
    @ConditionalOnMissingBean(name = "commonTaskExecutor")
    public ThreadPoolTaskExecutor commonTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(commonTaskExecutorProperties.getCorePoolSize());
        executor.setMaxPoolSize(commonTaskExecutorProperties.getMaxPoolSize());
        executor.setQueueCapacity(commonTaskExecutorProperties.getQueueCapacity());
        executor.setThreadNamePrefix(commonTaskExecutorProperties.getThreadNamePrefix());
        executor.setAllowCoreThreadTimeOut(commonTaskExecutorProperties.getAllowCoreThreadTimeout());
        executor.setKeepAliveSeconds((int) commonTaskExecutorProperties.getKeepAliveSeconds().getSeconds());
        executor.initialize();
        return executor;
    }

    @Bean
    @ConditionalOnMissingBean(name = "commonTaskScheduler")
    public ThreadPoolTaskScheduler commonTaskScheduler() {
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.setPoolSize(commonTaskSchedulerProperties.getPoolSize());
        scheduler.setThreadNamePrefix(commonTaskSchedulerProperties.getThreadNamePrefix());
        scheduler.initialize();
        return scheduler;
    }
}
