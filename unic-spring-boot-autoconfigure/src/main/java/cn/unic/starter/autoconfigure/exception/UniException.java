package cn.unic.starter.autoconfigure.exception;

/**
 * @author CloudS3n
 * @date 2021-06-11 10:41
 */
public class UniException extends RuntimeException{

    public UniException(String msg) {
        super(msg);
    }

    public UniException(String msg, Throwable e) {
        super(msg, e);
    }
}
