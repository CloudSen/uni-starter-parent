package cn.uni.starter.storage.minio;

/**
 * MINIO 异常类
 *
 * @author clouds3n
 * @since 2022-03-10
 */
public class UniMinioException extends Exception {

    public UniMinioException(String message, Throwable cause) {
        super(message, cause);
    }
}
