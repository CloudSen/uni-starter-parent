package cn.uni.starter.redisson.customizer;

import org.redisson.codec.JsonJacksonCodec;
import org.redisson.config.Config;
import org.redisson.spring.starter.RedissonAutoConfigurationCustomizer;
import org.springframework.stereotype.Component;

/**
 * 序列化配置
 *
 * @author CloudS3n
 * @date 2021-08-01 16:15
 */
@Component
@SuppressWarnings("DuplicatedCode")
public class SerializerCustomizer implements RedissonAutoConfigurationCustomizer {

    @Override
    public void customize(Config configuration) {
        configuration.setCodec(new JsonJacksonCodec());
    }
}
