package cn.unic.starter.autoconfigure.annotation.mvc;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.core.annotation.AliasFor;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.lang.annotation.*;

/**
 * @author CloudS3n
 * @date 2021-06-22 15:34
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@EnableFeignClients
@EnableJpaRepositories
@EntityScan
@SpringBootApplication
public @interface SpringCloudApplication {

    @AliasFor(annotation = SpringBootApplication.class, attribute = "scanBasePackages")
    String componentBasePackages() default "cn.unic.*";

    @AliasFor(annotation = EntityScan.class, attribute = "basePackages")
    String entityBasePackages() default "cn.unic.*";

    @AliasFor(annotation = EnableJpaRepositories.class, attribute = "basePackages")
    String repositoryBasePackages() default "cn.unic.*";

    @AliasFor(annotation = EnableFeignClients.class, attribute = "basePackages")
    String feignBasePackages() default "cn.unic.*";
}
