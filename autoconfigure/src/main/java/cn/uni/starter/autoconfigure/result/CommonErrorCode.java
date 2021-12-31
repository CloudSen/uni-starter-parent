package cn.uni.starter.autoconfigure.result;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;

/**
 * 通用错误码
 *
 * @author clouds3n
 * @since 2021-12-13
 */
@Getter
@RequiredArgsConstructor
public enum CommonErrorCode implements ErrorCode {

    /**
     * 系统未知错误 COMMON_00000
     */
    INTERNAL_ERROR(COMMON_CODE, COMMON_MSG),
    TOKEN_INVALIDED("COMMON_00001", "无法获取用户信息，请确保token的有效性"),
    /**
     * 认证失败 COMMON_00401
     */
    UNAUTHORIZED("COMMON_00401", "认证失败"),
    /**
     * 无操作权限 COMMON_00403
     */
    ACCESS_DENIED("COMMON_00403", "无操作权限"),
    ;

    private final String code;
    private final String msg;

    public static String parseMsg(String code) {
        return Arrays.stream(values())
            .filter(e -> StringUtils.equals(e.code, code))
            .findAny()
            .map(CommonErrorCode::getMsg)
            .orElse(COMMON_MSG);
    }

    public static ErrorCode parseEnum(String code) {
        return Arrays.stream(values())
            .filter(e -> StringUtils.equals(e.code, code))
            .findAny()
            .orElse(INTERNAL_ERROR);
    }
}
