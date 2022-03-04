package cn.uni.starter.log.aspect;

import cn.uni.starter.log.properties.UniLogEnableProperties;
import cn.uni.starter.log.publisher.ErrorLogPublisher;
import cn.uni.starter.log.utils.UrlUtil;
import cn.uni.starter.log.utils.WebUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * 异常日志异步入库
 *
 * @author <bailong>
 * @date 2022-03-03
 */
@Aspect
@Component
@RequiredArgsConstructor
@Log4j2
public class ErrorLogAspect {

    private final UniLogEnableProperties uniLogEnableProperties;

    @Pointcut("execution( public * cn.uni.starter.autoconfigure.exception.DefaultExceptionHandlerAutoConfiguration.*(..))")
    public void aPointcut() {
    }

    @Before("aPointcut()")
    public void beforeAdvice(JoinPoint point) {
        if (!uniLogEnableProperties.isErrorLogEnabled()) {
            log.info("异步写入error日志配置为关闭状态");
            return;
        }
        Exception e = (Exception) point.getArgs()[0];
        //发送服务异常事件
        ErrorLogPublisher.publishEvent(e, UrlUtil.getPath(WebUtil.getRequest().getRequestURI()));
    }
}
