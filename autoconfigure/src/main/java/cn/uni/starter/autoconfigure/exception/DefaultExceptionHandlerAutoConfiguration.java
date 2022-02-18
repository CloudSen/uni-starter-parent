package cn.uni.starter.autoconfigure.exception;

import cn.uni.starter.autoconfigure.AutoConfigConstants;
import cn.uni.starter.autoconfigure.result.CommonErrorCode;
import cn.uni.starter.autoconfigure.result.ErrorCode;
import cn.uni.starter.autoconfigure.result.Res;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
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
        e.getBindingResult().getAllErrors().stream().limit(1).forEach(error -> msg.append(error.getDefaultMessage()).append(","));
        return Res.error(StringUtils.isBlank(msg.toString()) ? AutoConfigConstants.SERVER_ERROR : msg.toString());
    }

    @ExceptionHandler(UniException.class)
    public Res<?> uniExceptionHandler(UniException e) {
        log.error(ExceptionUtils.getStackTrace(e));
        ErrorCode commonErrorCode = Optional.ofNullable(e.getErrorCode()).orElse(CommonErrorCode.INTERNAL_ERROR);
        return Res.error(commonErrorCode.getCode(), commonErrorCode.getMsg());
    }

    @ExceptionHandler(BindException.class)
    public Res<?> bindExceptionHandler(BindException e) {
        log.error(ExceptionUtils.getStackTrace(e));
        List<ObjectError> allErrors = e.getAllErrors();
        if (CollectionUtils.isNotEmpty(allErrors)) {
            return Res.error(allErrors.get(0).getDefaultMessage());
        }
        return Res.error(AutoConfigConstants.ERROR_OPERATE);
    }

    @ExceptionHandler(Exception.class)
    public Res<?> otherExceptionHandler(Exception e) {
        log.error(ExceptionUtils.getStackTrace(e));
        return Res.error(AutoConfigConstants.ERROR_OPERATE);
    }

    private Res<?> unauthorizedHandler(Exception e) {
        log.error(ExceptionUtils.getStackTrace(e));
        return Res.unauthorized();
    }
}
