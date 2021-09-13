package cn.uni.starter.autoconfigure.exception;

import lombok.Getter;

/**
 * @author CloudS3n
 * @date 2021-06-11 10:41
 */
@Getter
public class UniException extends RuntimeException {

    private String code;

    public UniException(String msg) {
        super(msg);
    }

    public UniException(String msg, String code) {
        super(msg);
        this.code = code;
    }

    public UniException(String msg, Throwable e) {
        super(msg, e);
    }

    public UniException(String msg, Throwable e, String code) {
        super(msg, e);
        this.code = code;
    }
}
