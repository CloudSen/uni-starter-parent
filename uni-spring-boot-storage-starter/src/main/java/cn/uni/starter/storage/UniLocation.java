package cn.uni.starter.storage;

import cn.uni.starter.storage.minio.UniMinioStorageLocation;

import java.net.URI;

/**
 * Represents a resource location
 *
 * @author clouds3n
 * @since 2022-03-14
 */
public interface UniLocation {

    /**
     * Check if the location references a bucket and not a blob.
     *
     * @return if the location describes a bucket
     */
    boolean isBucket();

    /**
     * Returns whether this {@link UniLocation} represents a file or not.
     *
     * @return true if the location describes a file
     */
    boolean isFile();

    /**
     * Returns whether this {@link UniLocation} represents a folder.
     *
     * @return true if the location describes a folder
     */
    boolean isFolder();

    /**
     * Returns the bucket name.
     *
     * @return the name of the bucket
     */
     String getBucketName();

    /**
     * Returns the path to the blob/folder relative from the bucket root. Returns null
     * if the {@link UniMinioStorageLocation} specifies a bucket itself.
     *
     * @return a path to the blob or folder; null if the location is to a bucket.
     */
     String getBlobName();

    /**
     * Returns the URI of the location.
     *
     * @return the URI object of the location.
     */
    URI uri();

    /**
     * Returns the URI string for the location.
     *
     * @return the URI string of the location.
     */
    String uriString();
}
