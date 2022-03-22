package cn.uni.starter.storage.minio;

import cn.hutool.core.io.FileUtil;
import cn.uni.starter.storage.AbstractUniResource;
import cn.uni.starter.storage.UniLocation;
import cn.uni.starter.storage.model.vo.FileMetadataVO;
import io.minio.*;
import io.minio.http.Method;
import io.minio.messages.DeleteObject;
import io.minio.messages.Item;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.util.Assert;

import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;
import java.util.*;

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
    private final Duration preSignedExpire;

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
     * @param preSignedExpire Define the expiry time for shared url, defaults 10 min
     * @throws IllegalArgumentException if the location URI is invalid
     * @see #MinioStorageUniResource(MinioClient, UniMinioStorageLocation, boolean, Duration)
     */
    public MinioStorageUniResource(MinioClient client, String locationUri, boolean autoCreateFiles, Duration preSignedExpire) {
        this(client, new UniMinioStorageLocation(locationUri), autoCreateFiles, preSignedExpire);
    }

    /**
     * Constructor that defaults autoCreateFiles to true, and preSignedExpire to 10 min.
     *
     * @param locationUri the cloud storage address
     * @param client      the storage client
     * @see #MinioStorageUniResource(MinioClient, String, boolean, Duration)
     */
    public MinioStorageUniResource(MinioClient client, String locationUri) {
        this(client, locationUri, true, Duration.ofMinutes(10));
    }

    /**
     * Constructs the resource representation of a bucket or a blob (file) in MINIO Storage.
     *
     * @param client          the MINIO Storage client
     * @param location        the {@link UniMinioStorageLocation} of the resource.
     * @param autoCreateFiles determines the auto-creation of the file in MINIO Storage
     *                        if an operation that depends on its existence is triggered (e.g., getting the
     *                        output stream of a file)
     * @param preSignedExpire Define the expiry time for shared url, defaults 10 min
     * @throws IllegalArgumentException if the location is an invalid MINIO Storage location
     */
    public MinioStorageUniResource(
        MinioClient client,
        UniMinioStorageLocation location,
        boolean autoCreateFiles,
        Duration preSignedExpire) {
        super(location);
        Assert.notNull(client, "Storage object can not be null");
        this.client = client;
        this.location = location;
        this.autoCreateFiles = autoCreateFiles;
        this.preSignedExpire = preSignedExpire;
    }
    //</editor-fold>

    public boolean isAutoCreateFiles() {
        return this.autoCreateFiles;
    }

    /**
     * Gets the underlying storage object in MINIO Storage.
     *
     * @return the storage object metadata, will be null if it does not exist in MINIO Storage.
     */
    @Override
    public Optional<FileMetadataVO> getObjectMetadata() {
        StatObjectResponse statObjectResponse;
        String previewUrl;
        try {
            statObjectResponse = client.statObject(StatObjectArgs.builder()
                .bucket(getEncodedBucketName())
                .object(getEncodedBlobName()).build());
        } catch (Exception e) {
            log.error(UniMinioConstants.MINIO_ERROR, ExceptionUtils.getStackTrace(e));
            statObjectResponse = null;
        }
        try {
            previewUrl = client.getPresignedObjectUrl(GetPresignedObjectUrlArgs.builder()
                .method(Method.GET)
                .bucket(getEncodedBucketName())
                .object(getEncodedBlobName())
                .expiry(Math.toIntExact(preSignedExpire.getSeconds()))
                .build());
        } catch (Exception e) {
            log.error(UniMinioConstants.MINIO_ERROR, ExceptionUtils.getStackTrace(e));
            previewUrl = StringUtils.EMPTY;
        }
        if (Objects.isNull(statObjectResponse)) {
            return Optional.empty();
        }
        FileMetadataVO metadataVO = new FileMetadataVO();
        BeanUtils.copyProperties(statObjectResponse, metadataVO);
        metadataVO.setSize(FileUtil.readableFileSize(statObjectResponse.size()))
            .setPreviewUrl(previewUrl)
            .setFilename(getFilename())
            .setMimeType(statObjectResponse.headers().get(UniMinioConstants.CONTENT_TYPE));
        return Optional.of(metadataVO);
    }

    @Override
    public Optional<FileMetadataVO> copyObject() {
        throw new UnsupportedOperationException(UniMinioConstants.MINIO_ERROR);
    }

    @Override
    public String getPreSignedUploadUrl() {
        throw new UnsupportedOperationException(UniMinioConstants.MINIO_ERROR);
    }

    @Override
    public Map<String, String> getPreSignedPostFormData() {
        throw new UnsupportedOperationException(UniMinioConstants.MINIO_ERROR);
    }

    @Override
    public FileMetadataVO putThenReturnObject() {
        putObject();
        return getObjectMetadata().orElse(FileMetadataVO.NULL_METADATA);
    }

    @Override
    public boolean putObject() {
        try {
            client.putObject(PutObjectArgs.builder()
                .bucket(getEncodedBucketName())
                .stream(getObjectStream(), -1, 5243000L)
                .build());
            return true;
        } catch (Exception e) {
            log.error(UniMinioConstants.MINIO_ERROR, ExceptionUtils.getStackTrace(e));
            return false;
        }
    }

    @Override
    public boolean removeObject() {
        try {
            client.removeObject(RemoveObjectArgs.builder()
                .bucket(getEncodedBucketName())
                .object(getEncodedBlobName()).build());
            return true;
        } catch (Exception e) {
            log.error(UniMinioConstants.MINIO_ERROR, ExceptionUtils.getStackTrace(e));
            return false;
        }
    }

    @Override
    public boolean removeObjects() {
        try {
            Set<AbstractUniResource> relatedResources = this.getRelatedResources();
            List<DeleteObject> objects = new LinkedList<>();
            objects.add(new DeleteObject(this.getEncodedBlobName()));
            relatedResources.stream().map(r -> new DeleteObject(r.getEncodedBlobName())).forEach(objects::add);
            client.removeObjects(RemoveObjectsArgs.builder()
                .bucket(this.getEncodedBucketName()).objects(objects).build());
            return true;
        } catch (Exception e) {
            log.error(UniMinioConstants.MINIO_ERROR, ExceptionUtils.getStackTrace(e));
            return false;
        }
    }

    @Override
    public boolean uploadSnowballObjects() {
        throw new UnsupportedOperationException(UniMinioConstants.MINIO_ERROR);
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
    public List<FileMetadataVO> listObjects() {
        List<FileMetadataVO> metadataList = new ArrayList<>();
        try {
            Iterable<Result<Item>> items = client.listObjects(ListObjectsArgs.builder()
                .bucket(getEncodedBucketName())
                .maxKeys(20)
                .build());
            for (Result<Item> res : items) {
                Item item = res.get();
                metadataList.add(
                    new FileMetadataVO()
                        .setFilename(item.objectName().substring(getBlobName().lastIndexOf(UniMinioConstants.SLASH)))
                        .setSize(FileUtil.readableFileSize(item.size()))
                        .setIsDir(false)
                        .setBucket(getBucketName())
                        .setObject(item.objectName())
                        .setLastModified(item.lastModified())
                );
            }
        } catch (Exception e) {
            log.error(UniMinioConstants.MINIO_ERROR, ExceptionUtils.getStackTrace(e));
        }
        return metadataList;
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
