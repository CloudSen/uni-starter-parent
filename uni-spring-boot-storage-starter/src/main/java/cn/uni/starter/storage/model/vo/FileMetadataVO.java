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
     * object name
     */
    private String filename;
}
