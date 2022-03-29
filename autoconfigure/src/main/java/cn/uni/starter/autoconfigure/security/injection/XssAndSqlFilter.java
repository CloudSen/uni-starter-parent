package cn.uni.starter.autoconfigure.security.injection;

import cn.uni.common.util.Response;
import com.alibaba.fastjson.JSON;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author clouds3n
 * @since 2022-03-29
 */
@Component
@Order(1)
@Log4j2
public class XssAndSqlFilter implements Filter {

    static {
        log.info("[ 自动装配 ] 正在配置XSS&SQL INJECTION 过滤器...");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        XssAndSqlHttpServletRequestWrapper xssRequest = new XssAndSqlHttpServletRequestWrapper(request);
        String method = request.getMethod();
        String param = "";
        log.info("[ FILTER ] 开始XSS&SQL Injection校验...");
        if ("POST".equalsIgnoreCase(method)) {
            param = this.getBodyString(xssRequest.getReader());
            if (StringUtils.isNotBlank(param) && xssRequest.checkXssAndSql(param)) {
                servletResponse.setCharacterEncoding("UTF-8");
                servletResponse.setContentType("application/json;charset=UTF-8");
                PrintWriter out = servletResponse.getWriter();
                out.write(JSON.toJSONString(Response.resp(Response.ERROR, "非法参数")));
            }
        }
        if (xssRequest.checkParameter()) {
            servletResponse.setCharacterEncoding("UTF-8");
            servletResponse.setContentType("application/json;charset=UTF-8");
            PrintWriter out = servletResponse.getWriter();
            out.write(JSON.toJSONString(Response.resp(Response.ERROR, "非法参数")));
            return;
        }
        filterChain.doFilter(xssRequest, servletResponse);
    }

    /**
     * 获取request请求body中参数
     *
     * @param br BufferedReader
     * @return body参数
     */
    private String getBodyString(BufferedReader br) throws IOException {
        String inputLine;
        StringBuilder str = new StringBuilder();
        try (br) {
            while ((inputLine = br.readLine()) != null) {
                str.append(inputLine);
            }
        }
        return str.toString();
    }

}
