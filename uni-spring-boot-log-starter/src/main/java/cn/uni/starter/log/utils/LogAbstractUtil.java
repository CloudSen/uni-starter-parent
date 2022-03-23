package cn.uni.starter.log.utils;

import cn.uni.starter.log.config.LogToolAutoConfig;
import cn.uni.starter.log.constant.LogConstant;
import cn.uni.starter.log.dto.UniLogAbstract;
import cn.uni.starter.log.server.ServerInfo;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.util.ObjectUtils;

import javax.servlet.http.HttpServletRequest;

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
    public static void addRequestInfoToLog(HttpServletRequest request, UniLogAbstract uniLogAbstract) {
        if (!ObjectUtils.isEmpty(request)) {
            //获取用户信息,设置authId
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null) {
                OAuth2Authentication authenticationToken = (OAuth2Authentication) authentication;
                if (!ObjectUtils.isEmpty(authenticationToken.getPrincipal())) {
                    uniLogAbstract.setUsername(authenticationToken.getPrincipal().toString());
                } else {
                    uniLogAbstract.setUsername("");
                }
            }
            uniLogAbstract.setRemoteIp(WebUtil.getIp(request));
            uniLogAbstract.setRequestUri(UrlUtil.getPath(request.getRequestURI()));
            uniLogAbstract.setMethod(request.getMethod());
            if ("POST".equals(request.getMethod())) {
                uniLogAbstract.setParams("");//WebUtil.getBodyString(request)
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
