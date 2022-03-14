package cn.uni.starter.storage.minio;

import cn.hutool.core.io.FileUtil;
import cn.uni.starter.storage.AbstractUniResource;
import cn.uni.starter.storage.UniLocation;
import cn.uni.starter.storage.model.vo.FileMetadataVO;
import io.minio.*;
import io.minio.http.Method;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.util.Assert;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * Represents a MINIO resource
 *
 * @author clouds3n
 * @since 2022-03-10
 */
@Slf4j
@EqualsAndHashCode(callSuper = true)
public class MinioStorageUniResource extends AbstractUniResource {

    private final MinioClient client;
    private final UniLocation location;
    private final boolean autoCreateFiles;

    //<editor-fold desc="Constructors">

    /**
     * Constructs the resource representation of a bucket or a blob (file) in MINIO Storage.
     *
     * @param client          the MINIO Storage client
     * @param locationUri     the URI of the bucket or blob, e.g., minio://your-bucket/ or
     *                        minio://your-bucket/your-file-name
     * @param autoCreateFiles determines the auto-creation of the file in MINIO Storage
     *                        if an operation that depends on its existence is triggered (e.g., getting the
     *                        output stream of a file)
     * @throws IllegalArgumentException if the location URI is invalid
     * @see #MinioStorageUniResource(MinioClient, UniMinioStorageLocation, boolean)
     */
    public MinioStorageUniResource(MinioClient client, String locationUri, boolean autoCreateFiles) {
        this(client, new UniMinioStorageLocation(locationUri), autoCreateFiles);
    }

    /**
     * Constructor that defaults autoCreateFiles to true.
     *
     * @param locationUri the cloud storage address
     * @param client      the storage client
     * @see #MinioStorageUniResource(MinioClient, String, boolean)
     */
    public MinioStorageUniResource(MinioClient client, String locationUri) {
        this(client, locationUri, true);
    }

    /**
     * Constructs the resource representation of a bucket or a blob (file) in MINIO Storage.
     *
     * @param client          the MINIO Storage client
     * @param location        the {@link UniMinioStorageLocation} of the resource.
     * @param autoCreateFiles determines the auto-creation of the file in MINIO Storage
     *                        if an operation that depends on its existence is triggered (e.g., getting the
     *                        output stream of a file)
     * @throws IllegalArgumentException if the location is an invalid MINIO Storage location
     */
    public MinioStorageUniResource(
        MinioClient client,
        UniMinioStorageLocation location,
        boolean autoCreateFiles) {
        super(location);
        Assert.notNull(client, "Storage object can not be null");
        this.client = client;
        this.location = location;
        this.autoCreateFiles = autoCreateFiles;
    }
    //</editor-fold>

    public boolean isAutoCreateFiles() {
        return this.autoCreateFiles;
    }

    /**
     * Gets the underlying storage object in MINIO Storage.
     *
     * @return the storage object metadata, will be null if it does not exist in MINIO Storage.
     * @throws UniMinioException if an issue occurs getting the Blob
     */
    @Override
    public Optional<FileMetadataVO> getBlobMetadata() throws Exception {
        StatObjectResponse statObjectResponse;
        String previewUrl;
        try {
            statObjectResponse = client.statObject(StatObjectArgs.builder()
                .bucket(getEncodedBucketName())
                .object(getEncodedBlobName()).build());
            previewUrl = client.getPresignedObjectUrl(GetPresignedObjectUrlArgs.builder()
                .method(Method.GET)
                .bucket(getEncodedBucketName())
                .object(getEncodedBlobName())
                .expiry(10, TimeUnit.MINUTES)
                .build());
        } catch (Exception e) {
            throw new UniMinioException("Failed to get MINIO object metadata", e);
        }
        if (Objects.isNull(statObjectResponse)) {
            return Optional.empty();
        }
        FileMetadataVO metadataVO = new FileMetadataVO();
        BeanUtils.copyProperties(statObjectResponse, metadataVO);
        metadataVO.setSize(FileUtil.readableFileSize(statObjectResponse.size()))
            .setPreviewUrl(previewUrl)
            .setFilename(getFilename());
        return Optional.of(metadataVO);
    }

    /**
     * @return true if bucket exists; false otherwise
     */
    @Override
    public boolean isBucketExists() {
        try {
            return client.bucketExists(BucketExistsArgs.builder().bucket(getEncodedBucketName()).build());
        } catch (Exception e) {
            log.error(UniMinioConstants.MINIO_ERROR, ExceptionUtils.getStackTrace(e));
            return false;
        }
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
            log.error(UniMinioConstants.MINIO_ERROR, ExceptionUtils.getStackTrace(e));
            return 0;
        }
    }

    @Override
    public long lastModified() throws IOException {
        try {
            return getBlobMetadata().map(f -> f.getLastModified().toEpochSecond()).orElse(0L);
        } catch (Exception e) {
            log.error(UniMinioConstants.MINIO_ERROR, ExceptionUtils.getStackTrace(e));
            return 0;
        }
    }

    @Override
    public String getDescription() {
        try {
            return getBlobMetadata().map(FileMetadataVO::toString).orElse(StringUtils.EMPTY);
        } catch (Exception e) {
            log.error(UniMinioConstants.MINIO_ERROR, ExceptionUtils.getStackTrace(e));
            return StringUtils.EMPTY;
        }
    }

    /**
     * Gets data from offset to length of an SSE-C encrypted object. Returned {@link InputStream} must
     * be closed after use to release network resources.
     *
     * <pre>Example:{@code
     * try (InputStream stream = resource.getInputStream()) {
     *   // Read data from stream
     * }}</pre>
     *
     * @return MINIO object stream
     */
    @Override
    public InputStream getInputStream() throws IOException {
        if (isBucket()) {
            throw new IllegalStateException("Cannot open an input stream to a bucket: '" + getURI() + "'");
        }
        try {
            return client.getObject(GetObjectArgs.builder()
                .bucket(getBucketName())
                .object(getBlobName())
                .build());
        } catch (Exception e) {
            log.error(UniMinioConstants.MINIO_ERROR, ExceptionUtils.getStackTrace(e));
        }
        return InputStream.nullInputStream();
    }
}
