package cn.uni.starter.autoconfigure.mybatis;

import cn.uni.starter.autoconfigure.AutoConfigConstants;
import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.log4j.Log4j2;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;
import java.util.Arrays;

/**
 * @author CloudS3n
 * @date 2021-06-11 09:59
 */
@Log4j2
@Configuration
@ConditionalOnBean({HikariDataSource.class, MybatisConfiguration.class})
@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
public class DefaultMybatisPlusAutoConfiguration {

    public DefaultMybatisPlusAutoConfiguration() {
        log.info(AutoConfigConstants.LOADING_MYBATIS_PLUS_AUTO_CONFIGURE);
    }

    @Bean("mybatisSqlSessionFactory")
    @ConditionalOnMissingBean
    public SqlSessionFactory sqlSessionFactory(DataSource dataSource) throws Exception {
        Resource[] resources = new PathMatchingResourcePatternResolver()
            .getResources("classpath*:mapper/**/*.xml");
        log.info(AutoConfigConstants.MYBATIS_PLUS_MAPPERS, Arrays.toString(resources).replaceAll(",", "\n"));
        MybatisSqlSessionFactoryBean factoryBean = new MybatisSqlSessionFactoryBean();
        factoryBean.setDataSource(dataSource);
        factoryBean.setMapperLocations(resources);
        factoryBean.setPlugins(paginationInterceptor());
        factoryBean.setFailFast(true);
        return factoryBean.getObject();
    }

    @Bean
    @ConditionalOnMissingBean
    public MybatisPlusInterceptor paginationInterceptor() {
        MybatisPlusInterceptor pagination = new MybatisPlusInterceptor();
        pagination.addInnerInterceptor(new PaginationInnerInterceptor());
        return pagination;
    }
}
