package cn.uni.starter.log.event;

import org.springframework.context.ApplicationEvent;

/**
 * Api日志事件
 *
 * @author <bailong>
 * @date 2022-03-03
 */
public class ApiLogEvent extends ApplicationEvent {
    public ApiLogEvent(Object source) {
        super(source);
    }
}
