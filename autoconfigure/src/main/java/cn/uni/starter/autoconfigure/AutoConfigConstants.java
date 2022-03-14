package cn.uni.starter.autoconfigure;

/**
 * @author CloudS3n
 * @date 2021-06-11 09:59
 */
public final class AutoConfigConstants {

    private AutoConfigConstants() {
    }

    public static final String PREFIX = "[ 自动装配 ] ";
    public static final String LOADING_ES_AUTO_CONFIGURE = PREFIX + "加载ES配置";
    public static final String ERROR_ES_CAN_NOT_PARSE_ADDRESS = PREFIX + "解析ES地址异常, {}";
    public static final String LOADING_THREAD_POOL_AUTO_CONFIGURE = PREFIX + "加载默认线程池配置";
    public static final String LOADING_MVC_AUTO_CONFIGURE = PREFIX + "加载默认MVC配置";
    public static final String LOADING_EXCEPTION_HANDLER_AUTO_CONFIGURE = PREFIX + "加载默认全局异常处理配置";
    public static final String LOADING_SERIALIZER_AUTO_CONFIGURE = PREFIX + "加载默认Jackson序列化配置";
    public static final String LOADING_OLD_REDIS_AUTO_CONFIGURE = PREFIX + "加载旧的Redis配置";
    public static final String LOADING_OLD_REDIS_TEMPLATE = PREFIX + "加载RedisTemplate配置";
    public static final String LOADING_OLD_REDIS_SERIALIZER = PREFIX + "加载Redis序列化配置";
    public static final String LOADING_OLD_REDIS_CACHE = PREFIX + "加载RedisCache配置";
    public static final String LOADING_REDISSON_AUTO_CONFIGURE = PREFIX + "加载Redisson配置";
    public static final String LOADING_REDISSON_CACHE_MGMT_AUTO_CONFIGURE = PREFIX + "加载Redisson cache manager配置";
    public static final String LOADING_TRANSACTION_AUTO_CONFIGURE = PREFIX + "加载MP事务配置";
    public static final String LOADING_MYBATIS_PLUS_AUTO_CONFIGURE = PREFIX + "加载MP配置";
    public static final String MYBATIS_PLUS_MAPPERS = PREFIX + "加载MP Mappers: {}";
    public static final String SUCCESS_OPERATE = "操作成功";
    public static final String ERROR_OPERATE = "操作失败";
    public static final String SERVER_ERROR = "服务端错误";
    public static final String NO_AUTHORIZATION = "没有权限";
    public static final String TRUE = "true";
    public static final String UNI_DEFAULT_CONFIG = "uni.autoconfigure.";
    public static final String UNI_DEFAULT_CONFIG_SECURITY = UNI_DEFAULT_CONFIG + "enable-security";
    public static final String UNI_DEFAULT_CONFIG_SERIALIZER = UNI_DEFAULT_CONFIG + "enable-serializer";
    public static final String LOADING_STORAGE_AUTO_CONFIGURE = PREFIX + "加载STORAGE配置";
}
