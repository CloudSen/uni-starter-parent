package cn.uni.starter.log.event;

import org.springframework.context.ApplicationEvent;

import java.util.Map;

/**
 * 错误日志事件
 *
 * @author <bailong>
 * @date 2022-03-02
 */
public class ErrorLogEvent extends ApplicationEvent {

    public ErrorLogEvent(Map<String, Object> source) {
        super(source);
    }
}
