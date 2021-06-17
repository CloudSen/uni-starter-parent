package cn.unic.starter.autoconfigure.es.properties;

import lombok.Data;
import org.hibernate.validator.constraints.Range;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Collections;
import java.util.List;

/**
 * @author CloudS3n
 * @date 2021-06-11 09:55
 */
@Data
@Validated
@Component
@ConfigurationProperties(prefix = "unic.config.es")
public class EsProperties {

    @NotEmpty
    private List<String> addresses = Collections.singletonList("127.0.0.1");

    @NotNull
    @Range(max = Integer.MAX_VALUE)
    private Integer restPort = 9200;

    @NotNull
    @Range(max = Integer.MAX_VALUE)
    private Integer commPort = 9300;

    @NotNull
    private Boolean enableSniff = true;

    @NotEmpty
    private String clusterName = "my-application";
}
