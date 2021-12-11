package cn.uni.starter.mp;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.Version;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 乐观锁字段
 *
 * @author clouds3n
 * @date 2021-12-10
 */
@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class VersionColumnDO implements Serializable {

    private static final long serialVersionUID = -4967569676085339813L;

    /**
     * 乐观锁字段
     */
    @Version
    @TableField("version")
    private Long version;
}
