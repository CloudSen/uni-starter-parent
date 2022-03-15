package cn.uni.starter.storage.local;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;

/**
 * @author tjt
 * @since 2022-03-15
 */
@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@Validated
@RefreshScope
@Component
@ConfigurationProperties("uni.storage.local")
public class UniLocalStorageProtocolResolverSettings {


    @NotBlank
    private String topPath;

    /**
     * Will create the bucket if it does not exist on the bucket path.
     */
    private boolean createBucket = true;
}
