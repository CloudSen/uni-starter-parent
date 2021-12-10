package cn.uni.starter.autoconfigure.serializer;

import cn.uni.starter.autoconfigure.AutoConfigConstants;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

/**
 * 序列化设置
 *
 * @author CloudS3n
 * @date 2021-06-17 19:09
 */
@Slf4j
@Configuration
@ConditionalOnProperty(name = AutoConfigConstants.UNI_DEFAULT_CONFIG_SERIALIZER, havingValue = AutoConfigConstants.TRUE)
public class SerializerAutoConfiguration {

    private static final String DATE_FORMAT = "yyyy-MM-dd";
    private static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private static final String DATE_TIME_WITH_T_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";
    private static final String STR_T = "'T'";

    static {
        log.info(AutoConfigConstants.LOADING_SERIALIZER_AUTO_CONFIGURE);
    }

    @Bean
    @ConditionalOnMissingBean
    public Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperBuilderCustomizer() {
        return builder -> {
            builder.deserializerByType(LocalDateTime.class, new LocalDateTimeDeserializer());
            builder.deserializerByType(LocalDate.class, new LocalDateDeserializer());
        };
    }

    /**
     * 反序列化LocalDateTime
     */
    public static class LocalDateTimeDeserializer extends JsonDeserializer<LocalDateTime> {
        @Override
        public LocalDateTime deserialize(JsonParser p, DeserializationContext deserializationContext)
            throws IOException {
            long timestamp = p.getValueAsLong();
            if (timestamp > 0) {
                return LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp), ZoneId.systemDefault());
            }
            String strTime = p.getValueAsString();
            if (StringUtils.isNotBlank(strTime) && strTime.contains(STR_T)) {
                return LocalDateTime.parse(strTime, DateTimeFormatter.ofPattern(DATE_TIME_WITH_T_FORMAT));
            } else {
                return LocalDateTime.parse(strTime, DateTimeFormatter.ofPattern(DATE_TIME_FORMAT));
            }
        }
    }

    /**
     * 反序列化LocalDate
     */
    public static class LocalDateDeserializer extends JsonDeserializer<LocalDate> {
        @Override
        public LocalDate deserialize(JsonParser p, DeserializationContext deserializationContext)
            throws IOException {
            long timestamp = p.getValueAsLong();
            if (timestamp > 0) {
                return Instant.ofEpochMilli(timestamp).atZone(ZoneId.systemDefault()).toLocalDate();
            }
            String strDate = p.getValueAsString();
            if (StringUtils.isNotBlank(strDate)) {
                return LocalDate.parse(strDate, DateTimeFormatter.ofPattern(DATE_FORMAT));
            }
            return null;
        }
    }
}
