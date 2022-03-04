package cn.uni.starter.log.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * Api日志DTO
 *
 * @author <bailong>
 * @date 2022-03-03
 */
@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class UniLogApiDTO extends UniLogAbstract {
    private static final long serialVersionUID = 4134518107462577774L;

    /**
     * 日志类型
     */
    private String type;

    /**
     * 日志标题
     */
    private String title;

    /**
     * 执行时间
     */
    private String time;
}
