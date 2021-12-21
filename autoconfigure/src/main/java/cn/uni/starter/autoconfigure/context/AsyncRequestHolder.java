package cn.uni.starter.autoconfigure.context;

import com.alibaba.ttl.TransmittableThreadLocal;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * 异步全局请求上下文
 * <p>
 * 慎
 * </p>
 *
 * @author clouds3n
 * @since 2021-12-20
 */
public final class AsyncRequestHolder {

    public static final ThreadLocal<Map<String, String>> TTL_THREAD_LOCAL;

    static {
        TTL_THREAD_LOCAL = new TransmittableThreadLocal<>() {
            @Override
            protected Map<String, String> initialValue() {
                return new HashMap<>(32);
            }
        };
    }

    public static Map<String, String> get() {
        return TTL_THREAD_LOCAL.get();
    }

    public static String get(String key) {
        return TTL_THREAD_LOCAL.get().get(key);
    }

    public static void set(String key, String value) {
        if (StringUtils.isAnyBlank(key, value)) {
            return;
        }
        TTL_THREAD_LOCAL.get().put(key, value);
    }

    public static void remove() {
        TTL_THREAD_LOCAL.remove();
    }
}
