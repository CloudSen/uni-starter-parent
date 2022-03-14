package cn.uni.starter.autoconfigure.storage;

import cn.uni.starter.autoconfigure.AutoConfigConstants;
import cn.uni.starter.storage.minio.UniMinioStorageProtocolResolver;
import cn.uni.starter.storage.minio.UniMinioStorageProtocolResolverSettings;
import io.minio.MinioClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * MINIO Spring Boot Auto-Configuration.
 * Also, it {@link Import} a {@link UniMinioStorageProtocolResolver} to register
 * it with the {@code DefaultResourceLoader}.
 *
 * @author clouds3n
 * @since 2022-03-10
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
@ConditionalOnClass({MinioClient.class, UniMinioStorageProtocolResolverSettings.class, UniMinioStorageProtocolResolver.class})
@ConditionalOnProperty(value = "uni.autoconfigure.enable-storage", matchIfMissing = true)
@EnableConfigurationProperties(UniMinioStorageProtocolResolverSettings.class)
@Import(UniMinioStorageProtocolResolver.class)
public class UniMinioAutoConfiguration {

    private final UniMinioStorageProtocolResolverSettings uniMinioStorageProtocolResolverSettings;

    static {
        log.info(AutoConfigConstants.LOADING_STORAGE_AUTO_CONFIGURE);
    }

    @Bean
    public MinioClient minioClient() {
        MinioClient minioClient = MinioClient.builder()
            .endpoint(uniMinioStorageProtocolResolverSettings.getEndpoint())
            .credentials(uniMinioStorageProtocolResolverSettings.getAccessKey(), uniMinioStorageProtocolResolverSettings.getSecretKey())
            .build();
        minioClient.setTimeout(
            uniMinioStorageProtocolResolverSettings.getConnectTimeout().toMillis(),
            uniMinioStorageProtocolResolverSettings.getWriteTimeout().toMillis(),
            uniMinioStorageProtocolResolverSettings.getReadTimeout().toMillis()
        );
        return minioClient;
    }
}
