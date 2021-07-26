package cn.uni.starter.feign;

import org.springframework.core.task.TaskDecorator;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

/**
 * 用于异步任务上下文传递
 * <p>注意：此处是浅拷贝！若父线程request执行完毕，会被销毁！</p>
 * <p>必须设置到Spring TaskExecutor中才能生效，并在@Asyc注解上指定线程池</p>
 *
 * @author CloudS3n
 * @date 2021-07-26 13:54
 */
public class AsyncTaskDecorator implements TaskDecorator {
    @Override
    public Runnable decorate(Runnable runnable) {
        RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
        return () -> {
            try {
                RequestContextHolder.setRequestAttributes(requestAttributes);
                runnable.run();
            } finally {
                RequestContextHolder.resetRequestAttributes();
            }
        };
    }
}
