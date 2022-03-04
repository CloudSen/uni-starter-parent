package cn.uni.starter.log.utils;

import cn.uni.starter.autoconfigure.context.SpringContext;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEvent;

/**
 * spring 工具类
 *
 * @author <bailong>
 * @date 2022-03-02
 */
@Log4j2
public class SpringEventUtil {

    private static ApplicationContext context;


    /**
     * 发布事件
     *
     * @param event 事件
     */
    public static void publishEvent(ApplicationEvent event) {
        context = SpringContext.getContext();
        if (context == null) {
            return;
        }
        try {
            context.publishEvent(event);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
}
