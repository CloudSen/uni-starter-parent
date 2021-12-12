package cn.uni.common.util;

import cn.hutool.http.HttpStatus;
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
     * 响应码，200为成功
     */
    private Integer code;

    /**
     * 消息
     */
    private String msg;

    /**
     * 响应数据
     */
    private T data;

    public static <T> Res<T> ok() {
        return new Res<T>().setCode(HttpStatus.HTTP_OK).setMsg("操作成功");
    }

    public static <T> Res<T> ok(T data) {
        return new Res<T>().setCode(HttpStatus.HTTP_OK).setMsg("操作成功").setData(data);
    }

    public static <T> Res<T> ok(String msg) {
        return new Res<T>().setCode(HttpStatus.HTTP_OK).setMsg(msg);
    }

    public static <T> Res<T> ok(String msg, T data) {
        return new Res<T>().setCode(HttpStatus.HTTP_OK).setMsg(msg).setData(data);
    }

    public static <T> Res<T> error() {
        return new Res<T>().setCode(HttpStatus.HTTP_INTERNAL_ERROR).setMsg("操作失败");
    }

    public static <T> Res<T> error(Integer code) {
        return new Res<T>().setCode(code).setMsg("操作失败");
    }

    public static <T> Res<T> error(String msg) {
        return new Res<T>().setCode(HttpStatus.HTTP_INTERNAL_ERROR).setMsg(msg);
    }

    public static <T> Res<T> error(T data) {
        return new Res<T>().setCode(HttpStatus.HTTP_INTERNAL_ERROR).setMsg("操作失败").setData(data);
    }

    public static <T> Res<T> error(Integer code, String msg) {
        return new Res<T>().setCode(code).setMsg(msg);
    }

    public static <T> Res<T> error(String msg, T data) {
        return new Res<T>().setCode(HttpStatus.HTTP_INTERNAL_ERROR).setMsg(msg).setData(data);
    }

    public static <T> Res<T> error(Integer code, String msg, T data) {
        return new Res<T>().setCode(code).setMsg(msg).setData(data);
    }

    public static <T> Res<T> unauthorized() {
        return new Res<T>().setCode(HttpStatus.HTTP_UNAUTHORIZED).setMsg("认证失败");
    }
}
