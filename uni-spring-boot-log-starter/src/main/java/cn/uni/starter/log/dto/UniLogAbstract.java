package cn.uni.starter.log.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 日志基础类
 *
 * @author <bailong>
 * @date 2022-03-02
 */
@Accessors(chain = true)
@Data
public class UniLogAbstract implements Serializable {

    private static final long serialVersionUID = 2794829397407804578L;

    /**
     * 日志来源
     */
    private String nameSpace;

    /**
     * 请求服务ip
     */
    private String serviceIp;

    /**
     * 请求服务名
     */
    private String serviceHost;

    /**
     * 系统环境
     */
    private String env;

    /**
     * 操作方式
     */
    private String method;

    /**
     * 请求uri
     */
    private String requestUri;

    /**
     * 操作人ip
     */
    private String remoteIp;

    /**
     * 方法所属类
     */
    private String methodClass;

    /**
     * 方法名称
     */
    private String methodName;

    /**
     * 请求数据
     */
    private String params;

}
