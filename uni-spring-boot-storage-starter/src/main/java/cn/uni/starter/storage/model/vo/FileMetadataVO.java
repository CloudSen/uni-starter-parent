package cn.uni.starter.storage.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.ZonedDateTime;

/**
 * Describe file metadata
 *
 * @author clouds3n
 * @since 2022-03-14
 */
@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class FileMetadataVO implements Serializable {

    private static final long serialVersionUID = -3306607875013039895L;
    public static final FileMetadataVO NULL_METADATA = new FileMetadataVO();
    /**
     * file size
     */
    private String size;

    /**
     * last modified time
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private ZonedDateTime lastModified;

    /**
     * bucket name
     */
    private String bucket;

    /**
     * object path
     */
    private String object;

    /**
     * if object is file then return filename;
     * if object is path then return full path;
     * otherwise return bucket name
     */
    private String filename;

    /**
     * if underlying storage engine support preview, then return preview url; otherwise return empty String
     */
    private String previewUrl;

    /**
     * if object is directory, then ture; otherwise false or null
     */
    private Boolean isDir;
}
