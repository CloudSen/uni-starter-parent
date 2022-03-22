package cn.uni.starter.log.filter;

import lombok.extern.log4j.Log4j2;
import org.apache.commons.io.IOUtils;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.*;

/**
 * 过滤构造器
 *
 * @author <bailong>
 * @date 2022-03-22
 */
@Log4j2
public class ReHttpServletRequestWrapper extends HttpServletRequestWrapper {

    private byte[] bytes;
    private WrappedServletInputStream wrappedServletInputStream;

    public ReHttpServletRequestWrapper(HttpServletRequest request) {
        super(request);
        try {
            // 读取输入流里的请求参数，并保存到bytes里
            bytes = IOUtils.toByteArray(request.getInputStream());
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
            this.wrappedServletInputStream = new WrappedServletInputStream(byteArrayInputStream);
            reWriteInputStream();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    /**
     * 把参数重新写进请求里
     */
    public void reWriteInputStream() {
        wrappedServletInputStream.setStream(new ByteArrayInputStream(bytes != null ? bytes : new byte[0]));
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        return wrappedServletInputStream;
    }

    @Override
    public BufferedReader getReader() throws IOException {
        return new BufferedReader(new InputStreamReader(wrappedServletInputStream));
    }

    /**
     * 获取post参数，可以自己再转为相应格式
     */
    public String getRequestParams() throws IOException {
        return new String(bytes, this.getCharacterEncoding());
    }

    private class WrappedServletInputStream extends ServletInputStream {

        private InputStream stream;

        public WrappedServletInputStream(InputStream stream) {
            this.stream = stream;
        }

        public void setStream(InputStream stream) {
            this.stream = stream;
        }

        @Override
        public int read() throws IOException {
            return stream.read();
        }

        @Override
        public boolean isFinished() {
            return true;
        }

        @Override
        public boolean isReady() {
            return true;
        }

        @Override
        public void setReadListener(ReadListener readListener) {
        }

    }
}
