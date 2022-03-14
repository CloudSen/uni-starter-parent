package cn.uni.starter.storage;

import cn.uni.starter.storage.minio.UniMinioConstants;
import cn.uni.starter.storage.model.vo.FileMetadataVO;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.core.io.AbstractResource;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.Optional;

/**
 * Represents the customized resource, which extends {@link AbstractResource},
 * and enhanced some useful methods.
 *
 * @author clouds3n
 * @since 2022-03-14
 */
@Slf4j
@EqualsAndHashCode(callSuper = true)
@RequiredArgsConstructor
public abstract class AbstractUniResource extends AbstractResource {

    private final UniLocation location;

    /**
     * Check location is a bucket
     *
     * @return true if the resource is a bucket; false otherwise
     */
    public boolean isBucket() {
        return this.location.isBucket();
    }

    /**
     * @return Returns whether this resource represents a file or not
     */
    @Override
    public boolean isFile() {
        return location.isFile();
    }

    /**
     * @return Returns whether this resource represents a folder or not
     */
    public boolean isFolder() {
        return location.isFolder();
    }

    @Override
    public String getFilename() {
        if (isBucket()) {
            return getBucketName();
        }
        if (isFolder()) {
            return getBlobName();
        }
        return parseFileName();
    }

    private String parseFileName() {
        return getBlobName().substring(getBlobName().lastIndexOf(UniMinioConstants.SLASH));
    }

    /**
     * Get bucket name from location
     *
     * @return the bucket name of the MINIO Resource
     */
    public String getBucketName() {
        return this.location.getBucketName();
    }

    /**
     * @return the encoded bucket name of the MINIO Resource
     */
    public String getEncodedBucketName() {
        return this.encodeUri(this.location.getBucketName());
    }

    /**
     * @return the blob name of the MINIO Resource; null if the resource is a bucket
     */
    public String getBlobName() {
        return this.location.getBlobName();
    }

    /**
     * @return the encoded blob name of the MINIO Resource; null if the resource is a bucket
     */
    public String getEncodedBlobName() {
        return this.encodeUri(this.location.getBlobName());
    }

    /**
     * Gets the underlying storage object in MINIO Storage.
     *
     * @return the storage object metadata, will be null if it does not exist in MINIO Storage.
     * @throws Exception if an issue occurs getting the Blob
     */
    public abstract Optional<FileMetadataVO> getBlobMetadata() throws Exception; // NOSONAR java:S112

    /**
     * Check bucket exists
     *
     * @return true if bucket exists; false otherwise
     */
    public abstract boolean isBucketExists();

    /**
     * translates special characters from the URL
     *
     * @param uri uri location for resource
     * @return translated string
     */
    public String encodeUri(String uri) {
        return UriComponentsBuilder.fromUriString(uri).build().encode().toString();
    }

    @Override
    public boolean exists() {
        try {
            return isBucket() ? isBucketExists() : getBlobMetadata().isPresent();
        } catch (Exception e) {
            log.error(UniMinioConstants.MINIO_ERROR, ExceptionUtils.getStackTrace(e));
            return false;
        }
    }

    /**
     * @return true if resource is not a bucket and exists in MINIO storage; false otherwise
     */
    @Override
    public boolean isReadable() {
        return !isBucket() && exists();
    }

    @Override
    @SuppressWarnings("AlibabaLowerCamelCaseVariableNaming")
    public URL getURL() throws IOException {
        return getURI().toURL();
    }

    @Override
    @SuppressWarnings("AlibabaLowerCamelCaseVariableNaming")
    public URI getURI() {
        return this.location.uri();
    }

    @Override
    public long contentLength() throws IOException {
        try {
            return getBlobMetadata().map(FileMetadataVO::getSize).map(Long::valueOf).orElse(0L);
        } catch (Exception e) {
            log.error(ExceptionUtils.getStackTrace(e));
            return 0;
        }
    }

    @Override
    public long lastModified() throws IOException {
        try {
            return getBlobMetadata().map(f -> f.getLastModified().toEpochSecond()).orElse(0L);
        } catch (Exception e) {
            log.error(ExceptionUtils.getStackTrace(e));
            return 0;
        }
    }

    @Override
    public String getDescription() {
        try {
            return getBlobMetadata().map(FileMetadataVO::toString).orElse(StringUtils.EMPTY);
        } catch (Exception e) {
            log.error(ExceptionUtils.getStackTrace(e));
            return StringUtils.EMPTY;
        }
    }
}
