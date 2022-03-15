package cn.uni.starter.storage.local;

import cn.uni.starter.storage.UniLocation;
import cn.uni.starter.storage.minio.UniMinioConstants;
import cn.uni.starter.storage.minio.UniMinioStorageLocation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * @author tjt
 * @since 2022-03-14
 */
public class UniLocalStorageLocation implements UniLocation {

    /**
     * A File protocol format string, first arg represent the parent directory path,
     * second argument represent the subPath.
     */
    private static final String FILE_URI_FORMAT = "file://%s/%s";

    private static final String SLASH = "/";

    /**
     * local parent directory name
     */
    private final String parentDirectPath;

    /**
     * local Subdirectory name
     */
    private final String subPath;


    private final URI uri;

    /**
     * parse uri String
     *
     * @param localLocationUriStr localLocationUriStr
     */
    public UniLocalStorageLocation(String localLocationUriStr) {
        try {
            Assert.isTrue(
                localLocationUriStr.startsWith("file://"),
                "A local Storage URI must start with file://");

            URI locationUri = new URI(localLocationUriStr);

            Assert.isTrue(
                locationUri.getAuthority() != null,
                "No parent directory specified in the location: " + localLocationUriStr);

            this.parentDirectPath = locationUri.getAuthority();
            this.subPath = getSubPathFromUri(locationUri);

            // ensure that if it's a parent directory handle, location ends with a '/'
            if (StringUtils.isBlank(this.subPath) && !localLocationUriStr.endsWith(SLASH)) {
                locationUri = new URI(localLocationUriStr + SLASH);
            }
            this.uri = locationUri;
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException("Invalid location: " + localLocationUriStr, e);
        }

    }

    private String getSubPathFromUri(URI locationUri) {
        String uriPath = locationUri.getPath();
        if (StringUtils.isBlank(uriPath) || uriPath.equals(SLASH)) {
            // This indicates that the path specifies the parent directory
            return StringUtils.EMPTY;
        } else {
            return uriPath.substring(1);
        }
    }


    /**
     * Returns {@link UniMinioStorageLocation} to a parentDirectPath
     *
     * @param parentDirectPath parentDirectPath
     * @return UniMinioStorageLocation
     */
    public static UniLocalStorageLocation forParentDirectPath(String parentDirectPath) {
        return new UniLocalStorageLocation(String.format(FILE_URI_FORMAT, parentDirectPath, ""));
    }

    /**
     * Returns {@link UniMinioStorageLocation} to a parentDirectPath
     *
     * @param parentDirectPath a parent directory path
     * @param subPath          subPath
     * @return UniMinioStorageLocation
     */
    public static UniLocalStorageLocation forFile(String parentDirectPath, String subPath) {
        Assert.notNull(subPath, "The subPath to a file must not be null.");
        return new UniLocalStorageLocation(String.format(FILE_URI_FORMAT, parentDirectPath, subPath));
    }

    /**
     * Returns {@link UniMinioStorageLocation} to a parentDirectPath
     *
     * @param parentDirectPath parentDirectPath
     * @param pathToFolder     sub Folder path
     * @return UniMinioStorageLocation
     */
    public static UniLocalStorageLocation forFolder(String parentDirectPath, String pathToFolder) {
        Assert.notNull(pathToFolder, "The path to a MINIO Storage folder must not be null.");
        if (!pathToFolder.endsWith(SLASH)) {
            pathToFolder += SLASH;
        }
        return new UniLocalStorageLocation(String.format(FILE_URI_FORMAT, parentDirectPath, pathToFolder));
    }

    @Override
    public boolean isBucket() {
        return StringUtils.isBlank(this.subPath);
    }

    @Override
    public boolean isFile() {
        return StringUtils.isNotBlank(this.subPath) && !this.subPath.endsWith(UniMinioConstants.SLASH);
    }

    @Override
    public boolean isFolder() {
        return StringUtils.isNotBlank(this.subPath) && this.subPath.endsWith(UniMinioConstants.SLASH);
    }

    @Override
    public String getBucketName() {
        return parentDirectPath;
    }

    @Override
    public String getBlobName() {
        return subPath;
    }

    @Override
    public URI uri() {
        return this.uri;
    }

    @Override
    public String uriString() {
        return String.format(FILE_URI_FORMAT, parentDirectPath, subPath);
    }
}
