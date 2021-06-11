package cn.unic.starter.autoconfigure.common;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author Awake
 * @date 2020-07-31 09:56
 **/
@Data
@ApiModel
public class Response<T> {

    public static final String SUCCESS = "success";
    public static final String ERROR = "error";
    public static final String VALIDATE_FAILURE = "validateFailure";
    public static final String VALIDATE_SUCCESS = "validateSuccess";

    @ApiModelProperty(value = "状态")
    private String status;

    @ApiModelProperty(value = "消息")
    private String msg;

    @ApiModelProperty(value = "数据")
    private T data;

    @ApiModelProperty(value = "状态码")
    private String code;


    public Response() {

    }

    public Response(String status, String msg) {
        this(status, msg, null);
    }

    public Response(String status, T object) {
        this(status, null, object);
    }

    public Response(String status, String msg, T object) {
        this.status = status;
        this.msg = msg;
        this.data = object;
    }

    public Response(String status, String msg, T object, String code) {
        this.status = status;
        this.msg = msg;
        this.data = object;
        this.code = code;
    }

    public static <T> Response<T> resp(String status) {
        return resp(status, null, null);
    }

    public static <T> Response<T> resp(String status, T data) {
        return resp(status, null, data);
    }

    public static <T> Response<T> resp(String status, String msg) {
        return resp(status, msg, null);
    }

    public static <T> Response<T> resp(String status, String msg, T data) {
        return resp(status, null, msg, data);
    }

    public static <T> Response<T> resp(String status, String code, String msg, T data) {
        Response<T> respBean = new Response<>();
        respBean.setStatus(status);
        respBean.setMsg(msg);
        respBean.setData(data);
        respBean.setCode(code);
        return respBean;
    }


    public static Response<?> sendValidateErrorMessage() {
        return sendValidateErrorMessage(null);
    }

    public static <T> Response<?> sendValidateErrorMessage(T object) {
        return sendValidateErrorMessage(VALIDATE_FAILURE, object);
    }

    public static <T> Response<?> sendValidateErrorMessage(String message, T object) {
        return Response.resp(SUCCESS, VALIDATE_FAILURE, message, object);
    }

    public static Response<?> sendSuccessMessage() {
        return sendSuccessMessage(null);
    }

    public static Response<?> sendSuccessMessage(Object object) {
        return sendSuccessMessage(SUCCESS, object);
    }

    public static Response<?> sendSuccessMessage(String message, Object object) {
        return Response.resp(SUCCESS, SUCCESS, message, object);
    }
}
