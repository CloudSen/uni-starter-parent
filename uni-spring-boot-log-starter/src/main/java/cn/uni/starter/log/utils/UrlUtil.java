package cn.uni.starter.log.utils;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * url处理工具类
 *
 * @author bailong
 * @date 2022-03-02
 */
public class UrlUtil {


    /**
     * 获取url路径
     *
     * @param uriStr 路径
     * @return url路径
     */
    public static String getPath(String uriStr) {
        URI uri;

        try {
            uri = new URI(uriStr);
        } catch (URISyntaxException var3) {
            throw new RuntimeException(var3);
        }

        return uri.getPath();
    }

}
