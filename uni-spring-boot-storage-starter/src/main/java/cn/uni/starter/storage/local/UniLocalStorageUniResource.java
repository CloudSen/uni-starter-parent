package cn.uni.starter.storage.local;

import cn.hutool.core.io.FileUtil;
import cn.uni.starter.storage.AbstractUniResource;
import cn.uni.starter.storage.UniLocation;
import cn.uni.starter.storage.model.vo.FileMetadataVO;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.io.Resource;
import org.springframework.util.Assert;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * @author tjt
 * @since 2022-03-15
 */
@Slf4j
@EqualsAndHashCode(callSuper = true)
public class UniLocalStorageUniResource extends AbstractUniResource {

    private final UniLocation location;

    private final String topPath;

    private final boolean isCreateBucket;

    public UniLocalStorageUniResource(String location, String topPath, boolean isCreateBucket) {
        this(new UniLocalStorageLocation(location), topPath, isCreateBucket);
    }

    public UniLocalStorageUniResource(UniLocation location, String topPath, boolean isCreateBucket) {
        super(location);
        this.location = location;
        this.topPath = topPath;
        this.isCreateBucket = isCreateBucket;
        // initialize top path
        Path path = Paths.get(topPath);
        if (!Files.exists(path)) {
            try {
                Files.createDirectories(path);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public Optional<FileMetadataVO> getObjectMetadata() throws Exception {
        Assert.isTrue(
            ObjectUtils.isNotEmpty(getBucketName()),
            "A local Storage bucket must exist");
        FileMetadataVO metadataVO = new FileMetadataVO();
        File file = new File(new String((topPath + File.separator + getBucketName() + File.separator + getBlobName()).getBytes(StandardCharsets.UTF_8), StandardCharsets.UTF_8));
        metadataVO.setBucket(getBucketName());
        metadataVO.setObject(getBlobName());
        metadataVO.setIsDir(true);
        // file is not directory
        if (!file.isDirectory()) {
            metadataVO.setIsDir(false);
            metadataVO.setFilename(file.getName());
            metadataVO.setSize(FileUtil.readableFileSize(file));
            Instant fileInstant = Files.getLastModifiedTime(Objects.requireNonNull(file.toPath(), "file")).toInstant();
            metadataVO.setLastModified(ZonedDateTime.ofInstant(fileInstant, ZoneId.of("Asia/Shanghai")));
        }
        return Optional.of(metadataVO);
    }

    @NotNull
    @Override
    public Optional<FileMetadataVO> copyObject() {
        throw new UnsupportedOperationException(UniLocalConstants.FILE_ERROR);
    }

    @NotNull
    @Override
    public String getPreSignedUploadUrl() {
        throw new UnsupportedOperationException(UniLocalConstants.FILE_ERROR);
    }

    @NotNull
    @Override
    public Map<String, String> getPreSignedPostFormData() {
        throw new UnsupportedOperationException(UniLocalConstants.FILE_ERROR);
    }

    @NotNull
    @Override
    public FileMetadataVO putThenReturnObject() {
        throw new UnsupportedOperationException(UniLocalConstants.FILE_ERROR);
    }

    @Override
    public boolean putObject() throws IOException {
        byte[] bytes = IOUtils.toByteArray(getObjectStream());
        Path path = Paths.get(topPath, getBucketName(), getBlobName());
        Files.write(path, bytes);
        return true;
    }

    @Override
    public boolean removeObject() {
        throw new UnsupportedOperationException(UniLocalConstants.FILE_ERROR);
    }

    @Override
    public boolean removeObjects() {
        throw new UnsupportedOperationException(UniLocalConstants.FILE_ERROR);
    }

    @Override
    public boolean uploadSnowballObjects(@NotNull List<Resource> otherResources) {
        throw new UnsupportedOperationException(UniLocalConstants.FILE_ERROR);
    }

    @Override
    public boolean isBucketExists() {
        Path path = Paths.get(topPath, getBucketName());
        return Files.exists(path) && Files.isDirectory(path);
    }

    @Override
    public List<FileMetadataVO> listObjects() {
        throw new UnsupportedOperationException(UniLocalConstants.FILE_ERROR);
    }

    @Override
    public File getFile() {
        Path path = Paths.get(topPath, getBucketName(), getBlobName());
        File file = new File(new String((topPath + File.separator + getBucketName() + File.separator + getBlobName()).getBytes(StandardCharsets.UTF_8), StandardCharsets.UTF_8));
        Assert.isTrue(
            Files.exists(path),
            "A local Storage File must be exist");
        Assert.isTrue(
            !Files.isDirectory(path),
            "A local Storage File type must be file");
        return file;
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return new FileInputStream(getFile());
    }
}
