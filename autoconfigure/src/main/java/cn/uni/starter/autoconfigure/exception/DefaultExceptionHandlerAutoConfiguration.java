package cn.uni.starter.autoconfigure.exception;

import cn.uni.common.util.Response;
import cn.uni.starter.autoconfigure.AutoConfigConstants;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author CloudS3n
 * @date 2021-06-11 09:57
 */
@Log4j2
@RestController
@ControllerAdvice
@ConditionalOnProperty(name = "uni.autoconfigure.enable-exception-handler", havingValue = "true")
public class DefaultExceptionHandlerAutoConfiguration {

    static {
        log.info(AutoConfigConstants.LOADING_EXCEPTION_HANDLER_AUTO_CONFIGURE);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Response<?> validationExceptionHandler(MethodArgumentNotValidException e) {
        log.error(ExceptionUtils.getStackTrace(e));
        StringBuilder msg = new StringBuilder();
        e.getBindingResult().getAllErrors().forEach(error -> msg.append(error.getDefaultMessage()).append(","));
        return Response.resp(Response.ERROR, StringUtils.isBlank(msg.toString()) ? AutoConfigConstants.SERVER_ERROR : msg.toString());
    }

    @ExceptionHandler(UniException.class)
    public Response<?> uniExceptionHandler(UniException e) {
        log.error(ExceptionUtils.getStackTrace(e));
        String message = e.getMessage();
        return Response.resp(Response.ERROR, StringUtils.isBlank(message) ? AutoConfigConstants.ERROR_OPERATE : message);
    }

    @ExceptionHandler(Exception.class)
    public Response<?> otherExceptionHandler(Exception e) {
        log.error(ExceptionUtils.getStackTrace(e));
        return Response.resp(Response.ERROR, AutoConfigConstants.ERROR_OPERATE);
    }
}
