package cn.uni.starter.log.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;


/**
 * 错误日志
 *
 * @author <bailong>
 * @date 2022-03-02
 */
@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class UniLogErrorDTO extends UniLogAbstract {

    private static final long serialVersionUID = -4596613647539800707L;


    /**
     * 堆栈信息
     */
    private String stackTrace;

    /**
     * 异常名
     */
    private String exceptionName;

    /**
     * 异常信息
     */
    private String message;

    /**
     * 代码行数
     */
    private Integer lineNumber;

    /**
     * 文件名
     */
    private String fileName;

}
