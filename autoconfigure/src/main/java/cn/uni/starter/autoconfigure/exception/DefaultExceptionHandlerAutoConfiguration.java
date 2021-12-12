package cn.uni.starter.autoconfigure.exception;

import cn.hutool.http.HttpStatus;
import cn.uni.common.util.Res;
import cn.uni.starter.autoconfigure.AutoConfigConstants;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

/**
 * @author CloudS3n
 * @date 2021-06-11 09:57
 */
@Slf4j
@RestController
@ControllerAdvice
@ConditionalOnProperty(name = "uni.autoconfigure.enable-exception-handler", havingValue = "true")
public class DefaultExceptionHandlerAutoConfiguration {

    static {
        log.info(AutoConfigConstants.LOADING_EXCEPTION_HANDLER_AUTO_CONFIGURE);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Res<?> validationExceptionHandler(MethodArgumentNotValidException e) {
        log.error(ExceptionUtils.getStackTrace(e));
        StringBuilder msg = new StringBuilder();
        e.getBindingResult().getAllErrors().forEach(error -> msg.append(error.getDefaultMessage()).append(","));
        return Res.error(StringUtils.isBlank(msg.toString()) ? AutoConfigConstants.SERVER_ERROR : msg.toString());
    }

    @ExceptionHandler(UniException.class)
    public Res<?> uniExceptionHandler(UniException e) {
        log.error(ExceptionUtils.getStackTrace(e));
        String message = e.getMessage();
        int code = Optional.ofNullable(e.getCode()).filter(StringUtils::isNotBlank).map(Integer::parseInt).orElse(HttpStatus.HTTP_INTERNAL_ERROR);
        return Res.error(code, StringUtils.isBlank(message) ? AutoConfigConstants.ERROR_OPERATE : message);
    }

    @ExceptionHandler(Exception.class)
    public Res<?> otherExceptionHandler(Exception e) {
        log.error(ExceptionUtils.getStackTrace(e));
        return Res.error(AutoConfigConstants.ERROR_OPERATE);
    }
}
