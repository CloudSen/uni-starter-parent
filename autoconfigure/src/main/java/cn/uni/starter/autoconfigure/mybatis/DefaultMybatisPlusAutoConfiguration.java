package cn.uni.starter.autoconfigure.mybatis;

import cn.uni.starter.autoconfigure.AutoConfigConstants;
import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.OptimisticLockerInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author CloudS3n
 * @date 2021-06-11 09:59
 */
@Slf4j
@Configuration
@ConditionalOnClass({HikariDataSource.class, MybatisConfiguration.class})
public class DefaultMybatisPlusAutoConfiguration {

    public DefaultMybatisPlusAutoConfiguration() {
        log.info(AutoConfigConstants.LOADING_MYBATIS_PLUS_AUTO_CONFIGURE);
    }

    @Bean
    @ConditionalOnMissingBean
    public MybatisPlusInterceptor paginationInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor());
        interceptor.addInnerInterceptor(new OptimisticLockerInnerInterceptor());
        return interceptor;
    }
}
