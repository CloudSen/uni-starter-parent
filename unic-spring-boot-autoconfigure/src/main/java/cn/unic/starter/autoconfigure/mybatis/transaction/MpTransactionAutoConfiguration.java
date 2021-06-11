package cn.unic.starter.autoconfigure.mybatis.transaction;

import cn.unic.starter.autoconfigure.AutoConfigConstants;
import cn.unic.starter.autoconfigure.mybatis.DefaultMybatisPlusAutoConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

/**
 * @author CloudS3n
 * @date 2021-06-11 10:23
 */
@Slf4j
@Configuration
@ConditionalOnBean(DefaultMybatisPlusAutoConfiguration.class)
@AutoConfigureAfter(DefaultMybatisPlusAutoConfiguration.class)
@EnableTransactionManagement
public class MpTransactionAutoConfiguration {

    public MpTransactionAutoConfiguration() {
        log.info(AutoConfigConstants.LOADING_TRANSACTION_AUTO_CONFIGURE);
    }

    @Bean
    @ConditionalOnMissingBean
    public PlatformTransactionManager transactionManager(DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }
}
