package cn.uni.starter.storage.local;

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

import javax.validation.constraints.NotNull;
import java.util.Objects;

/**
 * @author tjt
 * @since 2022-03-15
 */
@Slf4j
public class UniLocalStorageProtocolResolver implements ProtocolResolver, BeanFactoryPostProcessor, ResourceLoaderAware {

    public static final String PROTOCOL = "file://";

    @NotNull
    private UniLocalStorageProtocolResolverSettings settings;
    @Nullable
    private ConfigurableListableBeanFactory beanFactory;

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {
        this.beanFactory = configurableListableBeanFactory;
    }

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        if (DefaultResourceLoader.class.isAssignableFrom(resourceLoader.getClass())) {
            ((DefaultResourceLoader) resourceLoader).addProtocolResolver(this);
        } else {
            log.warn("The provided delegate resource loader is not an implementation " + "of DefaultResourceLoader. Custom Protocol using file:// prefix will not be enabled.");
        }
    }

    private UniLocalStorageProtocolResolverSettings getSettings() {
        if (this.settings == null) {
            this.settings = Objects.requireNonNull(this.beanFactory).getBean("uniLocalStorageProtocolResolverSettings", UniLocalStorageProtocolResolverSettings.class);
        }
        return this.settings;
    }

    @Override
    public Resource resolve(String location, ResourceLoader resourceLoader) {
        if (!location.startsWith(PROTOCOL)) {
            return null;
        }
        Assert.notNull(location, "location string can not null");
        Assert.notNull(getSettings().getTopPath(), "TopPath  can not null");
        return new UniLocalStorageUniResource(location, getSettings().getTopPath(), getSettings().isCreateBucket());
    }
}
