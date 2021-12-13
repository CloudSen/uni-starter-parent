package cn.uni.starter.autoconfigure.result;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 统一响应JSON对象
 *
 * @author clouds3n
 * @since 2021-12-12
 */
@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class Res<T> implements Serializable {

    private static final long serialVersionUID = -709109828798783625L;

    /**
     * 是否成功
     */
    private Boolean success;

    /**
     * 错误代码
     */
    private String code;

    /**
     * 消息
     */
    private String msg;

    /**
     * 响应数据
     */
    private T data;

    private static final String OPT_SUCCESS = "操作成功";
    private static final String OPT_ERROR = "操作失败";

    public static <T> Res<T> ok() {
        return ok(OPT_SUCCESS);
    }

    public static <T> Res<T> ok(T data) {
        return ok(OPT_SUCCESS, data);
    }

    public static <T> Res<T> ok(String msg) {
        return ok(msg, null);
    }

    public static <T> Res<T> ok(String msg, T data) {
        return new Res<T>().setSuccess(true).setMsg(msg).setData(data);
    }

    public static <T> Res<T> error() {
        return new Res<T>().setSuccess(false).setMsg(OPT_ERROR);
    }

    public static <T> Res<T> error(String msg) {
        return error(CommonErrorCode.INTERNAL_ERROR.getCode(), OPT_ERROR);
    }

    public static <T> Res<T> error(T data) {
        return error(CommonErrorCode.INTERNAL_ERROR.getCode(), null ,data);
    }

    public static <T> Res<T> error(String code, String msg) {
        return error(code, msg, null);
    }

    public static <T> Res<T> error(String msg, T data) {
        return error(CommonErrorCode.INTERNAL_ERROR.getCode(), msg, data);
    }

    public static <T> Res<T> error(String code, String msg, T data) {
        return new Res<T>().setSuccess(false).setCode(code).setMsg(msg).setData(data);
    }

    public static <T> Res<T> unauthorized() {
        return new Res<T>().setSuccess(false).setCode(CommonErrorCode.UNAUTHORIZED.getCode()).setMsg(CommonErrorCode.UNAUTHORIZED.getMsg());
    }
}
