package cn.uni.starter.log.utils;

import cn.uni.starter.log.constant.LogConstant;
import lombok.extern.log4j.Log4j2;
import org.springframework.lang.Nullable;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.util.WebUtils;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.function.Predicate;

/**
 * Web工具类
 *
 * @author <bailong>
 * @date 2022-03-02
 */
@Log4j2
public class WebUtil extends WebUtils {

    private byte[] bytes;

    private static final String[] IP_HEADER_NAMES = new String[]{
        "x-forwarded-for",
        "Proxy-Client-IP",
        "WL-Proxy-Client-IP",
        "HTTP_CLIENT_IP",
        "HTTP_X_FORWARDED_FOR"
    };

    private static final Predicate<String> IP_PREDICATE = (ip) -> !StringUtils.hasText(ip) || LogConstant.UNKNOWN.equalsIgnoreCase(ip);


    /**
     * 获取ip
     *
     * @param request HttpServletRequest
     * @return {String}
     */
    @Nullable
    public static String getIp(@Nullable HttpServletRequest request) {
        if (request == null) {
            return LogConstant.EMPTY;
        }
        String ip = null;
        for (String ipHeader : IP_HEADER_NAMES) {
            ip = request.getHeader(ipHeader);
            if (!IP_PREDICATE.test(ip)) {
                break;
            }
        }
        if (IP_PREDICATE.test(ip)) {
            ip = request.getRemoteAddr();
        }
        //分割字符串，删除空白符
        return !StringUtils.hasText(ip) ? null : StringUtils.delimitedListToStringArray(ip, LogConstant.COMMA, " \t\n\n\f")[0];
    }

    /**
     * 获取 request 请求内容
     *
     * @param request request
     * @return {String}
     */
    public static String getRequestContent(HttpServletRequest request) {
        try {
            String queryString = request.getQueryString();
            if (StringUtils.hasText(queryString)) {
                return new String(queryString.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8).replaceAll("&amp;", "&").replaceAll("%22", "\"");
            }
            String charEncoding = request.getCharacterEncoding();
            if (charEncoding == null) {
                charEncoding = LogConstant.UTF_8;
            }
            byte[] buffer = getRequestBody(request.getInputStream()).getBytes();
            String str = new String(buffer, charEncoding).trim();
            if (!StringUtils.hasText(str)) {
                StringBuilder sb = new StringBuilder();
                Enumeration<String> parameterNames = request.getParameterNames();
                while (parameterNames.hasMoreElements()) {
                    String key = parameterNames.nextElement();
                    String value = request.getParameter(key);
                    UniLogStringUtil.appendBuilder(sb, key, "=", value, "&");
                }
                str = UniLogStringUtil.removeSuffix(sb.toString(), "&");
            }
            return str.replaceAll("&amp;", "&");
        } catch (Exception ex) {
            log.error(ex.getStackTrace());
            return LogConstant.EMPTY;
        }
    }


    /**
     * 获取 request 请求体
     *
     * @param servletInputStream servletInputStream
     * @return body
     */
    public static String getRequestBody(ServletInputStream servletInputStream) {
        StringBuilder sb = new StringBuilder();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(servletInputStream, StandardCharsets.UTF_8));
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            log.error(e.getStackTrace());
        } finally {
            if (servletInputStream != null) {
                try {
                    servletInputStream.close();
                } catch (IOException e) {
                    log.error(e.getStackTrace());
                }
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    log.error(e.getStackTrace());
                }
            }
        }
        return sb.toString();
    }

    /**
     * 获取请求Body
     *
     * @param request
     * @return
     */
    public static String getBodyString(final HttpServletRequest request) {
        StringBuilder sb = new StringBuilder();
        InputStream inputStream = null;
        BufferedReader reader = null;
        try {
            inputStream = request.getInputStream();
            reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
            String line = "";
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    log.error(e.getStackTrace());
                }
            }
            /*if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                   log.error(e.getStackTrace());
                }
            }*/
        }
        return sb.toString();
    }


    public static HttpServletRequest getRequest() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        return (requestAttributes == null) ? null : ((ServletRequestAttributes) requestAttributes).getRequest();
    }

}
