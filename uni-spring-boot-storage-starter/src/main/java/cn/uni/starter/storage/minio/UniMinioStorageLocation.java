package cn.uni.starter.storage.minio;

import cn.uni.starter.storage.UniLocation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * Represents a MINIO Storage location provided by the user.
 *
 * @author clouds3n
 * @since 2022-03-10
 */
public class UniMinioStorageLocation implements UniLocation {

    /**
     * A MINIO protocol format string, first arg represent the bucket,
     * second argument represent the object(file).
     */
    private static final String MINIO_URI_FORMAT = "minio://%s/%s";

    /**
     * MINIO bucket name
     */
    private final String bucketName;

    /**
     * MINIO blob name
     */
    private final String blobName;

    private final URI uri;

    /**
     * parse uri string
     */
    public UniMinioStorageLocation(String minioLocationUriString) {

        try {
            Assert.isTrue(
                minioLocationUriString.startsWith("minio://"),
                "A MINIO Storage URI must start with minio://");

            URI locationUri = new URI(minioLocationUriString);

            Assert.isTrue(
                locationUri.getAuthority() != null,
                "No bucket specified in the location: " + minioLocationUriString);

            this.bucketName = locationUri.getAuthority();
            this.blobName = getBlobPathFromUri(locationUri);

            // ensure that if it's a bucket handle, location ends with a '/'
            if (StringUtils.isBlank(this.blobName) && !minioLocationUriString.endsWith(UniMinioConstants.SLASH)) {
                locationUri = new URI(minioLocationUriString + UniMinioConstants.SLASH);
            }
            this.uri = locationUri;
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException("Invalid location: " + minioLocationUriString, e);
        }
    }

    /**
     * Check if the location references a bucket and not a blob.
     *
     * @return if the location describes a bucket
     */
    @Override
    public boolean isBucket() {
        return StringUtils.isBlank(this.blobName);
    }

    /**
     * Returns whether this {@link UniMinioStorageLocation} represents a file or not.
     *
     * @return true if the location describes a file
     */
    @Override
    public boolean isFile() {
        return StringUtils.isNotBlank(this.blobName) && !this.blobName.endsWith(UniMinioConstants.SLASH);
    }

    /**
     * Returns whether this {@link UniMinioStorageLocation} represents a folder.
     *
     * @return true if the location describes a folder
     */
    @Override
    public boolean isFolder() {
        return StringUtils.isNotBlank(this.blobName) && this.blobName.endsWith(UniMinioConstants.SLASH);
    }

    /**
     * Returns the MINIO Storage bucket name.
     *
     * @return the name of the MINIO Storage bucket
     */
    @Override
    public String getBucketName() {
        return bucketName;
    }

    /**
     * Returns the path to the blob/folder relative from the bucket root. Returns null
     * if the {@link UniMinioStorageLocation} specifies a bucket itself.
     *
     * @return a path to the blob or folder; null if the location is to a bucket.
     */
    @Override
    public String getBlobName() {
        return blobName;
    }

    /**
     * Returns the MINIO URI of the location.
     *
     * @return the URI object of the MINIO Storage location.
     */
    @Override
    public URI uri() {
        return this.uri;
    }

    /**
     * Returns the MINIO Storage URI string for the location.
     *
     * @return the URI string of the MINIO Storage location.
     */
    @Override
    public String uriString() {
        return String.format(MINIO_URI_FORMAT, bucketName, blobName);
    }

    @Override
    public String toString() {
        return uriString();
    }

    private static String getBlobPathFromUri(URI minioUri) {
        String uriPath = minioUri.getPath();
        if (StringUtils.isBlank(uriPath) || uriPath.equals(UniMinioConstants.SLASH)) {
            // This indicates that the path specifies the root of the bucket
            return StringUtils.EMPTY;
        } else {
            return uriPath.substring(1);
        }
    }

    /**
     * Returns a {@link UniMinioStorageLocation} to a bucket.
     *
     * @param bucketName name of the MINIO bucket
     * @return the {@link UniMinioStorageLocation} to the location.
     */
    public static UniMinioStorageLocation forBucket(String bucketName) {
        return new UniMinioStorageLocation(String.format(MINIO_URI_FORMAT, bucketName, ""));
    }

    /**
     * Returns a {@link UniMinioStorageLocation} for a file within a bucket.
     *
     * @param bucketName name of the MINIO bucket
     * @param pathToFile path to the file within the bucket
     * @return the {@link UniMinioStorageLocation} to the location.
     */
    public static UniMinioStorageLocation forFile(String bucketName, String pathToFile) {
        Assert.notNull(pathToFile, "The path to a MINIO Storage file must not be null.");
        return new UniMinioStorageLocation(String.format(MINIO_URI_FORMAT, bucketName, pathToFile));
    }

    /**
     * Returns a {@link UniMinioStorageLocation} to a folder whose path is relative to the
     * bucket.
     *
     * @param bucketName   name of the GCS bucket.
     * @param pathToFolder path to the folder within the bucket.
     * @return the {@link UniMinioStorageLocation} to the location.
     */
    public static UniMinioStorageLocation forFolder(String bucketName, String pathToFolder) {
        Assert.notNull(pathToFolder, "The path to a MINIO Storage folder must not be null.");
        if (!pathToFolder.endsWith(UniMinioConstants.SLASH)) {
            pathToFolder += UniMinioConstants.SLASH;
        }
        return new UniMinioStorageLocation(String.format(MINIO_URI_FORMAT, bucketName, pathToFolder));
    }
}
