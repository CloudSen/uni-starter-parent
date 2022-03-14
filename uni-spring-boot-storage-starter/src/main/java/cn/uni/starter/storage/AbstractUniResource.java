package cn.uni.starter.storage;

import cn.uni.starter.storage.model.vo.FileMetadataVO;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.AbstractResource;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Optional;

/**
 * Represents the customized resource, which extends {@link AbstractResource},
 * and enhanced some useful methods.
 *
 * @author clouds3n
 * @since 2022-03-14
 */
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
        return getBlobName().substring(getBlobName().lastIndexOf("/"));
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
}
