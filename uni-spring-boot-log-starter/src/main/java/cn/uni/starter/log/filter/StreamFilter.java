package cn.uni.starter.log.filter;

import lombok.extern.log4j.Log4j2;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * http请求过滤器
 *
 * @author <bailong>
 * @date 2022-03-22
 */
@Log4j2
public class StreamFilter /*implements Filter*/ {
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        //重新构建request
        ReHttpServletRequestWrapper requestWrapper = new ReHttpServletRequestWrapper((HttpServletRequest) servletRequest);
        filterChain.doFilter(requestWrapper, servletResponse);
    }
}
