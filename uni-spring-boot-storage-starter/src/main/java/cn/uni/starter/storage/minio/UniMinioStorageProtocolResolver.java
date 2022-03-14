package cn.uni.starter.storage.minio;

import io.minio.MinioClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.ProtocolResolver;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

import java.util.Objects;

/**
 * A {@link ProtocolResolver} implementation for the {@code minio://} protocol.
 *
 * @author clouds3n
 * @since 2022-03-10
 */
@Slf4j
public class UniMinioStorageProtocolResolver implements ProtocolResolver, BeanFactoryPostProcessor, ResourceLoaderAware {

    public static final String PROTOCOL = "minio://";

    @Nullable
    private MinioClient client;
    @Nullable
    private ConfigurableListableBeanFactory beanFactory;
    @Nullable
    private UniMinioStorageProtocolResolverSettings settings;

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {
        this.beanFactory = configurableListableBeanFactory;
    }

    private MinioClient getClient() {
        if (this.client == null) {
            this.client = Objects.requireNonNull(this.beanFactory).getBean(MinioClient.class);
        }
        return this.client;
    }

    private UniMinioStorageProtocolResolverSettings getSettings() {
        if (this.settings == null) {
            this.settings = Objects.requireNonNull(this.beanFactory).getBean("uniMinioStorageProtocolResolverSettings", UniMinioStorageProtocolResolverSettings.class);
        }
        return this.settings;
    }

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        if (DefaultResourceLoader.class.isAssignableFrom(resourceLoader.getClass())) {
            ((DefaultResourceLoader) resourceLoader).addProtocolResolver(this);
        } else {
            log.warn("The provided delegate resource loader is not an implementation " + "of DefaultResourceLoader. Custom Protocol using minio:// prefix will not be enabled.");
        }
    }

    @Nullable
    @Override
    public Resource resolve(String location, ResourceLoader resourceLoader) {
        if (!location.startsWith(PROTOCOL)) {
            return null;
        }
        Assert.notNull(getClient(), "minio client can not null");
        Assert.notNull(location, "location string can not null");
        Assert.notNull(getSettings().getPreSignedExpire(), "PreSignedExpire setting can not null");
        return new MinioStorageUniResource(getClient(), location, getSettings().isCreateBucket(), getSettings().getPreSignedExpire());
    }
}
