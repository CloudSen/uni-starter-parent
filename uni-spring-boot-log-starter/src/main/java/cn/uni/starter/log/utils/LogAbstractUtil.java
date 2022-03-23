package cn.uni.starter.log.utils;

import cn.uni.starter.log.config.LogToolAutoConfig;
import cn.uni.starter.log.constant.LogConstant;
import cn.uni.starter.log.dto.UniLogAbstract;
import cn.uni.starter.log.filter.ReHttpServletRequestWrapper;
import cn.uni.starter.log.server.ServerInfo;
import org.springframework.util.ObjectUtils;

/**
 * Log 工具类
 *
 * @author <bailong>
 * @date 2022-03-02
 */
public class LogAbstractUtil {

    /**
     * 向log中添加补齐request的信息
     *
     * @param request        request请求体
     * @param uniLogAbstract 日志基础类
     */
    public static void addRequestInfoToLog(ReHttpServletRequestWrapper request, UniLogAbstract uniLogAbstract) {
        if (!ObjectUtils.isEmpty(request)) {
            uniLogAbstract.setRemoteIp(WebUtil.getIp(request));
            //新版本日志服务用户信息暂时设定为空
            uniLogAbstract.setUsername("");
            uniLogAbstract.setRequestUri(UrlUtil.getPath(request.getRequestURI()));
            uniLogAbstract.setMethod(request.getMethod());
            if ("POST".equals(request.getMethod())) {
                uniLogAbstract.setParams(WebUtil.getBodyString(request));
            } else {
                uniLogAbstract.setParams(WebUtil.getRequestContent(request));
            }


        }
    }

    /**
     * 向log中添加补齐其他的信息
     *
     * @param uniLogAbstract 日志基础类
     * @param serverInfo     服务信息
     */
    public static void addOtherInfoToLog(UniLogAbstract uniLogAbstract, ServerInfo serverInfo) {
        uniLogAbstract.setNameSpace(LogToolAutoConfig.appName);
        uniLogAbstract.setServiceHost(serverInfo.getHostName());
        uniLogAbstract.setServiceIp(serverInfo.getIpWithPort());
        uniLogAbstract.setEnv(LogToolAutoConfig.env);
        if (uniLogAbstract.getParams() == null) {
            uniLogAbstract.setParams(LogConstant.EMPTY);
        }
    }
}
