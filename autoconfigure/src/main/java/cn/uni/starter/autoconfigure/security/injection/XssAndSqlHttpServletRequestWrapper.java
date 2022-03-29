package cn.uni.starter.autoconfigure.security.injection;

import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.StreamUtils;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.regex.Pattern;

/**
 * @author clouds3n
 * @since 2022-03-29
 */
@SuppressWarnings("DuplicatedCode")
@Log4j2
public class XssAndSqlHttpServletRequestWrapper extends HttpServletRequestWrapper {
    private static final String KEYWORD = "and|exec|insert|select|delete|update|count|*|%|chr|mid|master|truncate|char|declare|;|or|-|+";
    private static final Set<String> KEYWORD_BLACKLIST = new HashSet<>(16);

    static {
        String[] keyStr = KEYWORD.split("\\|");
        KEYWORD_BLACKLIST.addAll(Arrays.asList(keyStr));
    }

    private final Map<String, String[]> parameterMap;
    private final byte[] body;
    HttpServletRequest orgRequest = null;

    public XssAndSqlHttpServletRequestWrapper(HttpServletRequest request) throws IOException {
        super(request);
        orgRequest = request;
        parameterMap = request.getParameterMap();
        body = StreamUtils.copyToByteArray(request.getInputStream());
    }

    /**
     * 获取最原始的request的静态方法
     *
     * @return 原始的request
     */
    public static HttpServletRequest getOrgRequest(HttpServletRequest req) {
        if (req instanceof XssAndSqlHttpServletRequestWrapper) {
            return ((XssAndSqlHttpServletRequestWrapper) req).getOrgRequest();
        }
        return req;
    }

    /**
     * 半角字符直接替换成全角字符
     *
     * @param s 需要转换的字符串
     * @return 全角字符
     */
    private static String xssEncode(String s) {
        if (s == null || s.isEmpty()) {
            return s;
        } else {
            s = stripXssAndSql(s);
        }
        StringBuilder sb = new StringBuilder(s.length() + 16);
        for (int i = 0; i < s.length(); i++) {
            String c = String.valueOf(s.charAt(i));
            switch (c) {
                // 转义大于号
                case "&gt;":
                    sb.append("＞");
                    break;
                // 转义小于号
                case "&lt;":
                    sb.append("＜");
                    break;
                // 转义amp
                case "&amp;":
                    sb.append("＆");
                    break;
                // 转义#
                case "#":
                    sb.append("＃");
                    break;
                default:
                    sb.append(c);
                    break;
            }
        }
        return sb.toString();
    }

    /**
     * 防止xss跨脚本攻击
     */
    @SuppressWarnings("AlibabaAvoidPatternCompileInMethod")
    public static String stripXssAndSql(String value) {
        if (value != null) {
            // Avoid anything between script tags
            Pattern scriptPattern = Pattern.compile(
                "&lt;[\r\n| | ]*script[\r\n| | ]*&gt;(.*?)<!--[\r\n| | ]*script[\r\n| | ]*-->", Pattern.CASE_INSENSITIVE);
            value = scriptPattern.matcher(value).replaceAll("");
            // Avoid anything in a
            // src="http://www.yihaomen.com/article/java/..." type of
            // e-xpression
            scriptPattern = Pattern.compile("src[\r\n| | ]*=[\r\n| | ]*[\\\"|\\'](.*?)[\\\"|\\']",
                Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
            value = scriptPattern.matcher(value).replaceAll("");
            // Remove any lonesome  tag
            scriptPattern = Pattern.compile("<!--[\r\n| | ]*script[\r\n| | ]*-->", Pattern.CASE_INSENSITIVE);
            value = scriptPattern.matcher(value).replaceAll("");
            // Remove any lonesome <script ...> tag
            scriptPattern = Pattern.compile("<[\r\n| | ]*script(.*?)>",
                Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
            value = scriptPattern.matcher(value).replaceAll("");
            // Avoid eval(...) expressions
            scriptPattern = Pattern.compile("eval\\((.*?)\\)",
                Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
            value = scriptPattern.matcher(value).replaceAll("");
            // Avoid e-xpression(...) expressions
            scriptPattern = Pattern.compile("e-xpression\\((.*?)\\)",
                Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
            value = scriptPattern.matcher(value).replaceAll("");
            // Avoid javascript:... expressions
            scriptPattern = Pattern.compile("javascript[\r\n| | ]*:[\r\n| | ]*", Pattern.CASE_INSENSITIVE);
            value = scriptPattern.matcher(value).replaceAll("");
            // Avoid vbscript:... expressions
            scriptPattern = Pattern.compile("vbscript[\r\n| | ]*:[\r\n| | ]*", Pattern.CASE_INSENSITIVE);
            value = scriptPattern.matcher(value).replaceAll("");
            // Avoid onload= expressions
            scriptPattern = Pattern.compile("onload(.*?)=",
                Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
            value = scriptPattern.matcher(value).replaceAll("");
        }
        return value;
    }

    /**
     * 获取所有参数名
     *
     * @return 返回所有参数名
     */
    @Override
    public Enumeration<String> getParameterNames() {
        return Collections.enumeration(parameterMap.keySet());
    }

    /**
     * 将参数名和参数值都做xss sql过滤
     *
     * @param name 原始参数名
     * @return 处理后的参数名
     */
    @Override
    public String getParameter(String name) {
        String[] results = parameterMap.get(name);
        if (results == null || results.length <= 0) {
            return null;
        } else {
            String value = results[0];
            if (StringUtils.isNotBlank(value)) {
                value = xssEncode(value);
            }
            return value;
        }
    }

    /**
     * 获取指定参数名的所有值的数组，如：checkbox的所有数据 接收数组变量 ，如checkbox类型
     */
    @Override
    public String[] getParameterValues(String name) {
        String[] results = parameterMap.get(name);
        if (results == null || results.length <= 0) {
            return new String[0];
        } else {
            int length = results.length;
            for (int i = 0; i < length; i++) {
                results[i] = xssEncode(results[i]);
            }
            return results;
        }
    }

    /**
     * 覆盖getHeader方法，将参数名和参数值都做xss sql过滤
     */
    @Override
    public String getHeader(String name) {

        String value = super.getHeader(xssEncode(name));
        if (value != null) {
            value = xssEncode(value);
        }
        return value;
    }

    /**
     * 获取最原始的request
     *
     * @return 原始request
     */
    public HttpServletRequest getOrgRequest() {
        return orgRequest;
    }

    @SuppressWarnings({"AlibabaAvoidPatternCompileInMethod", "ConstantConditions"})
    public boolean checkXssAndSql(String value) {
        boolean flag = false;
        if (StringUtils.isBlank(value)) {
            return flag;
        }
        // Avoid anything between script tags
        Pattern scriptPattern = Pattern.compile(
            "<[\r\n| | ]*script[\r\n| | ]*>(.*?)</[\r\n| | ]*script[\r\n| | ]*>", Pattern.CASE_INSENSITIVE);
        flag = scriptPattern.matcher(value).find();
        if (flag) {
            return flag;
        }
        // Avoid anything in a
        // src="http://www.yihaomen.com/article/java/..." type of
        // e-xpression
        scriptPattern = Pattern.compile("src[\r\n| | ]*=[\r\n| | ]*[\\\"|\\'](.*?)[\\\"|\\']",
            Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
        flag = scriptPattern.matcher(value).find();
        if (flag) {
            return flag;
        }
        // Remove any lonesome </script> tag
        scriptPattern = Pattern.compile("<!--[\r\n| | ]*script[\r\n| | ]*-->", Pattern.CASE_INSENSITIVE);
        flag = scriptPattern.matcher(value).find();
        if (flag) {
            return flag;
        }
        // Remove any lonesome <script ...> tag
        scriptPattern = Pattern.compile("<[\r\n| | ]*script(.*?)>",
            Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
        flag = scriptPattern.matcher(value).find();
        if (flag) {
            return flag;
        }
        // Avoid eval(...) expressions
        scriptPattern = Pattern.compile("eval\\((.*?)\\)",
            Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
        flag = scriptPattern.matcher(value).find();
        if (flag) {
            return flag;
        }
        // Avoid e-xpression(...) expressions
        scriptPattern = Pattern.compile("e-xpression\\((.*?)\\)",
            Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
        flag = scriptPattern.matcher(value).find();
        if (flag) {
            return flag;
        }
        // Avoid javascript:... expressions
        scriptPattern = Pattern.compile("javascript[\r\n| | ]*:[\r\n| | ]*", Pattern.CASE_INSENSITIVE);
        flag = scriptPattern.matcher(value).find();
        if (flag) {
            return flag;
        }
        // Avoid vbscript:... expressions
        scriptPattern = Pattern.compile("vbscript[\r\n| | ]*:[\r\n| | ]*", Pattern.CASE_INSENSITIVE);
        flag = scriptPattern.matcher(value).find();
        if (flag) {
            return flag;
        }
        // Avoid onload= expressions
        scriptPattern = Pattern.compile("onload(.*?)=",
            Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
        flag = scriptPattern.matcher(value).find();
        if (flag) {
            return flag;
        }
        return checkSqlKeyWords(value);
    }

    public boolean checkSqlKeyWords(String value) {
        if (StringUtils.isBlank(value)) {
            return false;
        }
        for (String keyword : KEYWORD_BLACKLIST) {
            boolean invalid = value.length() > keyword.length() + 4
                && (value.contains(" " + keyword)
                || value.contains(keyword + " ")
                || value.contains(" " + keyword + " "));
            if (invalid) {
                log.error(this.getRequestURI() + "参数中包含不允许sql的关键词(" + keyword + ")");
                return true;
            }
        }
        return false;
    }

    public final boolean checkParameter() {
        Map<String, String[]> submitParams = new HashMap<>(parameterMap);
        Set<String> submitNames = submitParams.keySet();
        for (String submitName : submitNames) {
            Object submitValues = submitParams.get(submitName);
            if ((submitValues != null)) {
                for (String submitValue : (String[]) submitValues) {
                    if (checkXssAndSql(submitValue)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public BufferedReader getReader() throws IOException {
        return new BufferedReader(new InputStreamReader(getInputStream()));
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        final ByteArrayInputStream inputStream = new ByteArrayInputStream(body);
        return new ServletInputStream() {

            @Override
            public int read() throws IOException {
                return inputStream.read();
            }

            @Override
            public boolean isFinished() {
                return false;
            }

            @Override
            public boolean isReady() {
                return false;
            }

            @Override
            public void setReadListener(ReadListener arg0) {
                // 无需实现
            }
        };
    }
}
