package cn.unic.starter.autoconfigure.exception;

import cn.unic.starter.autoconfigure.AutoConfigConstants;
import cn.unic.starter.autoconfigure.common.Response;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
@RestController
@ControllerAdvice
@ConditionalOnProperty(name = "unic.config.default.enable-exception-handler", havingValue = "true")
public class DefaultExceptionHandlerAutoConfiguration {

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
        return Response.resp(Response.ERROR, StringUtils.isBlank(e.getMessage()) ? AutoConfigConstants.SERVER_ERROR : e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public Response<?> otherExceptionHandler(Exception e) {
        log.error(ExceptionUtils.getStackTrace(e));
        String msg = e.getCause() == null ? e.getMessage() : e.getCause().getMessage();
        if (StringUtils.isBlank(msg)) {
            msg = AutoConfigConstants.SERVER_ERROR;
        }
        return Response.resp(Response.ERROR, msg);
    }
}
