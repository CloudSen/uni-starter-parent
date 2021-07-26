package cn.uni.starter.autoconfigure.annotation.mvc;

import org.springframework.core.annotation.AliasFor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.annotation.*;

/**
 * @author CloudS3n
 * @date 2021-06-18 15:44
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@RestController
@Validated
@RequestMapping
public @interface RestValidController {

    @AliasFor(annotation = RequestMapping.class)
    String value() default "";
}
