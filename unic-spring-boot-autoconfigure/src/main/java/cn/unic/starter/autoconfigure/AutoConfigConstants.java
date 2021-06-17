package cn.unic.starter.autoconfigure;

/**
 * @author CloudS3n
 * @date 2021-06-11 09:59
 */
public final class AutoConfigConstants {

    public static final String PREFIX = "[ 自动装配 ] ";
    public static final String LOADING_ES_AUTO_CONFIGURE = PREFIX + "加载ES配置";
    public static final String ERROR_ES_CAN_NOT_PARSE_ADDRESS = PREFIX + "解析ES地址异常, {}";
    public static final String LOADING_THREAD_POOL_AUTO_CONFIGURE = PREFIX + "加载默认线程池配置";
    public static final String LOADING_TRANSACTION_AUTO_CONFIGURE = PREFIX + "加载MP事务配置";
    public static final String LOADING_MYBATIS_PLUS_AUTO_CONFIGURE = PREFIX + "加载MP配置";
    public static final String MYBATIS_PLUS_MAPPERS = PREFIX + "加载MP Mappers: {}";
    public static final String SUCCESS_OPERATE = "操作成功";
    public static final String ERROR_OPERATE = "操作异常";
    public static final String SERVER_ERROR = "服务端错误";
    public static final String NO_AUTHORIZATION = "没有权限";
    public static final String TRUE = "true";
    public static final String UNIC_DEFAULT_CONFIG = "unic.config.default.";
    public static final String UNIC_DEFAULT_CONFIG_SECURITY = UNIC_DEFAULT_CONFIG + "enable-security";
    public static final String UNIC_DEFAULT_CONFIG_SERIALIZER = UNIC_DEFAULT_CONFIG + "enable-serializer";
}
