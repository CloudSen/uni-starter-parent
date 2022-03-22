package cn.uni.starter.storage;

import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

/**
 * Tools to easy handle uni resource
 *
 * @author clouds3n
 * @since 2022-03-23
 */
public final class UniResourceUtil {

    private UniResourceUtil() {
    }

    /**
     * get customized resource from {@link ResourceLoader}
     *
     * @param resourceLoader 资源加载器
     * @param fileLocation   文件路径
     * @return mino resource
     */
    public static AbstractUniResource getResourceFrom(ResourceLoader resourceLoader, String fileLocation) {
        Resource resource = resourceLoader.getResource(fileLocation);
        boolean isUniResource = resource instanceof AbstractUniResource;
        if (!isUniResource) {
            throw new UnsupportedOperationException("Current resource can not found any AbstractUniResource implementation, plz check the location schema!");
        }
        return (AbstractUniResource) resource;
    }
}
