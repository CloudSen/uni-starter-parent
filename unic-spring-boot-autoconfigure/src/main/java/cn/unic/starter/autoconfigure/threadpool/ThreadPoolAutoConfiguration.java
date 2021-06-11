package cn.unic.starter.autoconfigure.threadpool;

import cn.unic.starter.autoconfigure.AutoConfigConstants;
import cn.unic.starter.autoconfigure.threadpool.properties.CommonTaskExecutorProperties;
import cn.unic.starter.autoconfigure.threadpool.properties.CommonTaskSchedulerProperties;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
@Configuration
@EnableConfigurationProperties({CommonTaskExecutorProperties.class, CommonTaskSchedulerProperties.class})
@ConditionalOnProperty(name = "unic.config.starter.enable-thread-pool", havingValue = "true")
public class ThreadPoolAutoConfiguration {

    public ThreadPoolAutoConfiguration() {
        log.info(AutoConfigConstants.LOADING_THREAD_POOL_AUTO_CONFIGURE);
    }

    @Bean
    @ConditionalOnProperty(name = "unic.config.starter.enable-thread-pool", havingValue = "true")
    public ThreadPoolTaskExecutor commonTaskExecutor(CommonTaskExecutorProperties properties) {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(properties.getCorePoolSize());
        executor.setMaxPoolSize(properties.getMaxPoolSize());
        executor.setQueueCapacity(properties.getQueueCapacity());
        executor.setThreadNamePrefix(properties.getThreadNamePrefix());
        executor.setAllowCoreThreadTimeOut(properties.getAllowCoreThreadTimeout());
        executor.setKeepAliveSeconds((int) properties.getKeepAliveSeconds().getSeconds());
        executor.initialize();
        return executor;
    }

    @Bean
    @ConditionalOnProperty(name = "unic.config.starter.enable-thread-pool", havingValue = "true")
    public ThreadPoolTaskScheduler commonTaskScheduler(CommonTaskSchedulerProperties properties) {
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.setPoolSize(properties.getPoolSize());
        scheduler.setThreadNamePrefix(properties.getThreadNamePrefix());
        scheduler.initialize();
        return scheduler;
    }
}
