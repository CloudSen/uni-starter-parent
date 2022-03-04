package cn.uni.starter.log.aspect;

import cn.uni.starter.log.annotation.ApiLog;
import cn.uni.starter.log.properties.UniLogEnableProperties;
import cn.uni.starter.log.publisher.ApiLogPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

/**
 * 操作日志使用event 异步入库
 *
 * @author <bailong>
 * @date 2022-03-03
 */
@Aspect
@RequiredArgsConstructor
@Log4j2
@Component
public class ApiLogAspect {
    private final UniLogEnableProperties uniLogEnableProperties;

    @Around("@annotation(apiLog)")
    public Object around(ProceedingJoinPoint point, ApiLog apiLog) throws Throwable {
        if (!uniLogEnableProperties.isApiLogEnabled()) {
            log.info("异步写入API日志配置为关闭状态");
            return log;
        }

        //获取类名
        String className = point.getTarget().getClass().getName();
        //获取方法
        String methodName = point.getSignature().getName();
        // 发送异步日志事件
        long beginTime = System.currentTimeMillis();
        //执行方法
        Object result = point.proceed();
        //执行时长(毫秒)
        long time = System.currentTimeMillis() - beginTime;
        //记录日志
        ApiLogPublisher.publishEvent(methodName, className, apiLog, time);
        return result;
    }
}
