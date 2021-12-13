package cn.uni.starter.autoconfigure.result;

/**
 * 错误代码
 * <p>NAMESPACE_00000</p>
 *
 * @author clouds3n
 * @since 2021-12-13
 */
public interface ErrorCode {

    public static final String COMMON_CODE = "COMMON00000";
    public static final String COMMON_MSG = "系统错误";

    /**
     * 获取当前系统自己的错误代码
     *
     * @return 错误代码
     */
    default String getCode() {
        return COMMON_CODE;
    }

    /**
     * 错误代码含义
     *
     * @return 错误代码含义
     */
    default String getMsg() {
        return COMMON_MSG;
    }
}
