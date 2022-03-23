package cn.uni.starter.storage;

import cn.uni.starter.storage.minio.UniMinioConstants;
import cn.uni.starter.storage.model.vo.FileMetadataVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.core.io.AbstractResource;
import org.springframework.lang.Nullable;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Represents the customized resource, which extends {@link AbstractResource},
 * and enhanced some useful methods.
 *
 * @author clouds3n
 * @since 2022-03-14
 */
@Slf4j
@RequiredArgsConstructor
public abstract class AbstractUniResource extends AbstractResource {

    private final UniLocation location;
    /**
     * Must set this when upload an object
     *
     * @see AbstractUniResource#setObjectStream(InputStream)
     * @see AbstractUniResource#putObject()
     */
    @Nullable
    private InputStream objectStream;
    @Nullable
    private Set<AbstractUniResource> relatedResources;

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

    /**
     * gets Object Stream
     *
     * @return InputStream
     */
    public InputStream getObjectStream() {
        return objectStream == null ? InputStream.nullInputStream() : objectStream;
    }

    /**
     * set Object Stream
     *
     * @param objectStream stream
     */
    public void setObjectStream(InputStream objectStream) {
        this.objectStream = objectStream;
    }

    public Set<AbstractUniResource> getRelatedResources() {
        return CollectionUtils.isEmpty(relatedResources) ? Collections.emptySet() : relatedResources;
    }

    public void setRelatedResources(Set<AbstractUniResource> relatedResources) {
        Set<String> schemas = relatedResources.stream().map(r -> r.getURI().getScheme()).collect(Collectors.toSet());
        if (schemas.size() != 1) {
            throw new UnsupportedOperationException(UniMinioConstants.MULTI_SCHEMA_ERROR);
        }
        this.relatedResources = relatedResources;
    }

    //<editor-fold desc="need to customize">

    /**
     * Gets the underlying storage object metadata in storage.
     *
     * @return the storage object metadata, will be null if it does not exist in storage.
     */
    public abstract Optional<FileMetadataVO> getObjectMetadata();

    /**
     * Creates an object by server-side copying data from another object.
     *
     * @return copied object metadata
     */
    public abstract Optional<FileMetadataVO> copyObject();

    /**
     * Gets pre-signed URL of an object for HTTP PUT method, expiry time and custom request parameters.
     *
     * @return pre-signed PUT URL to upload
     */
    public abstract String getPreSignedUploadUrl();

    /**
     * Gets form-data of PostPolicy of an object to upload its data using POST method.
     * Used to directly upload big file from front-end
     *
     * @return form-data map
     */
    public abstract Map<String, String> getPreSignedPostFormData();

    /**
     * Uploads given stream as object in bucket, and then return the object metadata.
     * Used to upload normal object stream from backend.
     *
     * @return the uploaded object metadata
     */
    public abstract FileMetadataVO putThenReturnObject();

    /**
     * Uploads given stream as object in bucket.
     * Used to upload normal file stream from backend.
     * NOTICE this method support sharding upload, part size defaults 5Mb.
     *
     * @return return true if success to upload an object
     */
    public abstract boolean putObject();

    /**
     * Removes an object.
     *
     * @return return true if success to remove an object
     */
    public abstract boolean removeObject();

    /**
     * Removes multiple objects.
     *
     * @return return true if success to remove all object
     */
    public abstract boolean removeObjects();

    /**
     * Uploads multiple objects
     *
     * @return return true if success to upload
     */
    public abstract boolean uploadSnowballObjects();

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
     */
    public abstract List<FileMetadataVO> listObjects();

    //</editor-fold>

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        AbstractUniResource that = (AbstractUniResource) o;
        return location.equals(that.location) && Objects.equals(objectStream, that.objectStream);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), location, objectStream);
    }
}
