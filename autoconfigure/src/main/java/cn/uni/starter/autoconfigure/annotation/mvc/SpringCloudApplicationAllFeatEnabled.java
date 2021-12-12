package cn.uni.starter.autoconfigure.annotation.mvc;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.core.annotation.AliasFor;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.lang.annotation.*;

/**
 * @author CloudS3n
 * @date 2021-06-24 11:57
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@MapperScan
@EnableFeignClients
@EnableJpaRepositories
@EntityScan
@SpringBootApplication
@EnableCaching
@EnableScheduling
@EnableAsync
@EnableTransactionManagement
public @interface SpringCloudApplicationAllFeatEnabled {

    @AliasFor(annotation = SpringBootApplication.class, attribute = "scanBasePackages")
    String[] componentBasePackages() default {"cn.uni.*"};

    @AliasFor(annotation = EntityScan.class, attribute = "basePackages")
    String[] entityBasePackages() default {"cn.uni.*"};

    @AliasFor(annotation = EnableJpaRepositories.class, attribute = "basePackages")
    String[] repositoryBasePackages() default {"cn.uni.*"};

    @AliasFor(annotation = EnableFeignClients.class, attribute = "basePackages")
    String[] feignBasePackages() default {"cn.uni.*"};

    @AliasFor(annotation = MapperScan.class, attribute = "basePackages")
    String[] mybatisBasePackages() default {"cn.uni.**.mapper"};
}
