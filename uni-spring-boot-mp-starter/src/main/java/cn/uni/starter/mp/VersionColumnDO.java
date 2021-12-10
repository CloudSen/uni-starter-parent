package cn.uni.starter.mp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

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
public class VersionColumnDO {

    /**
     * 乐观锁字段
     */
    private Long lockVersion;
}
