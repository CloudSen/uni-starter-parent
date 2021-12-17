package cn.uni.starter.autoconfigure.exception;

import cn.uni.starter.autoconfigure.result.CommonErrorCode;
import cn.uni.starter.autoconfigure.result.ErrorCode;
import lombok.Getter;

/**
 * @author CloudS3n
 * @date 2021-06-11 10:41
 */
@Getter
public class UniException extends RuntimeException {

    private final ErrorCode errorCode;

    public UniException(String msg) {
        super(msg);
        this.errorCode = CommonErrorCode.INTERNAL_ERROR;
    }

    public UniException(ErrorCode errorCode) {
        super(errorCode.getMsg());
        this.errorCode = errorCode;
    }

    public UniException(ErrorCode errorCode, Throwable e) {
        super(errorCode.getMsg(), e);
        this.errorCode = errorCode;
    }
}
