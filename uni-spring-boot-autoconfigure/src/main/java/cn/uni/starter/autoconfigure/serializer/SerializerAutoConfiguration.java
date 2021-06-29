package cn.uni.starter.autoconfigure.serializer;

import cn.uni.starter.autoconfigure.AutoConfigConstants;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import lombok.extern.log4j.Log4j2;
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

/**
 * 序列化设置
 *
 * @author CloudS3n
 * @date 2021-06-17 19:09
 */
@Log4j2
@Configuration
@ConditionalOnProperty(name = AutoConfigConstants.UNI_DEFAULT_CONFIG_SERIALIZER, havingValue = AutoConfigConstants.TRUE)
public class SerializerAutoConfiguration {

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
            } else {
                return null;
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
            } else {
                return null;
            }
        }
    }
}
