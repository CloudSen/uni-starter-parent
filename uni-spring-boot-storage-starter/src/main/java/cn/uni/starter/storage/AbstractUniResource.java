package cn.uni.starter.storage;

import cn.uni.starter.storage.minio.UniMinioConstants;
import cn.uni.starter.storage.model.vo.FileMetadataVO;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.core.io.AbstractResource;
import org.springframework.core.io.Resource;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.List;
import java.util.Map;
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
            return isBucket() ? isBucketExists() : getObjectMetadata().isPresent();
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
            return getObjectMetadata().map(FileMetadataVO::getSize).map(Long::valueOf).orElse(0L);
        } catch (Exception e) {
            log.error(ExceptionUtils.getStackTrace(e));
            return 0;
        }
    }

    @Override
    public long lastModified() throws IOException {
        try {
            return getObjectMetadata().map(f -> f.getLastModified().toEpochSecond()).orElse(0L);
        } catch (Exception e) {
            log.error(ExceptionUtils.getStackTrace(e));
            return 0;
        }
    }

    @Override
    public String getDescription() {
        try {
            return getObjectMetadata().map(FileMetadataVO::toString).orElse(StringUtils.EMPTY);
        } catch (Exception e) {
            log.error(ExceptionUtils.getStackTrace(e));
            return StringUtils.EMPTY;
        }
    }

    //<editor-fold desc="need to customize">

    /**
     * Gets the underlying storage object metadata in storage.
     *
     * @return the storage object metadata, will be null if it does not exist in storage.
     * @throws Exception if any issue occurs operating the object
     */
    public abstract Optional<FileMetadataVO> getObjectMetadata() throws Exception; // NOSONAR java:S112

    /**
     * Creates an object by server-side copying data from another object.
     *
     * @return copied object metadata
     * @throws Exception if any issue occurs operating the object
     */
    public abstract Optional<FileMetadataVO> copyObject() throws Exception; // NOSONAR java:S112

    /**
     * Gets pre-signed URL of an object for HTTP PUT method, expiry time and custom request parameters.
     *
     * @return pre-signed PUT URL to upload
     * @throws Exception if any issue occurs operating the object
     */
    public abstract String getPreSignedUploadUrl() throws Exception; // NOSONAR java:S112

    /**
     * Gets form-data of PostPolicy of an object to upload its data using POST method.
     * Used to directly upload big file from front-end
     *
     * @return form-data map
     * @throws Exception if any issue occurs operating the object
     */
    public abstract Map<String, String> getPreSignedPostFormData() throws Exception; // NOSONAR java:S112

    /**
     * Uploads given stream as object in bucket, and then return the object metadata.
     * Used to upload normal object stream from backend.
     *
     * @return the uploaded object metadata
     * @throws Exception if any issue occurs operating the object
     */
    public abstract FileMetadataVO putThenReturnObject() throws Exception; // NOSONAR java:S112

    /**
     * Uploads given stream as object in bucket.
     * Used to upload normal file stream from backend.
     *
     * @return return true if success to upload an object
     * @throws Exception if any issue occurs operating the object
     */
    public abstract boolean putObject() throws Exception; // NOSONAR java:S112

    /**
     * Removes an object.
     *
     * @return return true if success to remove an object
     * @throws Exception if any issue occurs operating the object
     */
    public abstract boolean removeObject() throws Exception; // NOSONAR java:S112

    /**
     * Removes multiple objects.
     *
     * @return return true if success to remove all object
     * @throws Exception if any issue occurs operating the object
     */
    public abstract boolean removeObjects() throws Exception; // NOSONAR java:S112

    /**
     * Uploads multiple objects
     *
     * @param otherResources other resources
     * @return return true if success to upload
     * @throws Exception if any issue occurs operating the object
     */
    public abstract boolean uploadSnowballObjects(List<Resource> otherResources) throws Exception; // NOSONAR java:S112

    /**
     * Check bucket exists.
     *
     * @return true if bucket exists; false otherwise
     */
    public abstract boolean isBucketExists();

    /**
     * Lists object information of a bucket.
     *
     * @return object metadata list
     * @throws Exception if any issue occurs operating the object
     */
    public abstract List<FileMetadataVO> listObjects() throws Exception; // NOSONAR java:S112
    //</editor-fold>
}
