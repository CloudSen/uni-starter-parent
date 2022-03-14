package cn.uni.starter.storage.minio;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import java.time.Duration;

/**
 * Minio settings holder for {@link UniMinioStorageProtocolResolver}
 * DO NOT provide bucket policy settings
 *
 * @author clouds3n
 * @since 2022-03-10
 */
@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@Validated
@RefreshScope
@Component
@ConfigurationProperties("uni.storage.minio")
public class UniMinioStorageProtocolResolverSettings {

    /**
     * URL for Minio instance. Can include the HTTP scheme. Must include the port. If the port is not provided, then the port of the HTTP is taken.
     */
    @NotBlank
    private String endpoint;

    /**
     * Access key (login) on Minio instance
     */
    @NotBlank
    private String accessKey;

    /**
     * Secret key (password) on Minio instance
     */
    @NotBlank
    private String secretKey;

    /**
     * Define the connection timeout for the Minio Client.
     */
    private Duration connectTimeout = Duration.ofSeconds(20);

    /**
     * Define the write operation timeout for the Minio Client.
     */
    private Duration writeTimeout = Duration.ofMinutes(2);

    /**
     * Define the read operation timeout for the Minio Client.
     */
    private Duration readTimeout = Duration.ofMinutes(2);

    /**
     * Will create the bucket if it does not exist on the Minio instance.
     */
    private boolean createBucket = true;

    private Duration duration;
}
