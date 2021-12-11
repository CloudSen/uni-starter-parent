package cn.uni.starter.mp;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 基础必备字段
 *
 * @author clouds3n
 * @date 2021-12-10
 */
@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class BaseColumnDO implements Serializable {

    private static final long serialVersionUID = -6311332723248181228L;

    @TableId
    private Long id;

    /**
     * 创建时间
     */
    @TableField("gmt_create")
    private LocalDateTime gmtCreate;

    /**
     * 更新时间
     */
    @TableField("gmt_modified")
    private LocalDateTime gmtModified;

    /**
     * 逻辑删除：1删除 0和null未删除
     */
    @TableLogic
    @TableField("deleted")
    private Boolean deleted;
}
