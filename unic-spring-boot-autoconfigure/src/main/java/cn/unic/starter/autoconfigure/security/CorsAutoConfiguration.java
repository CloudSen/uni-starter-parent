package cn.unic.starter.autoconfigure.security;

import cn.unic.starter.autoconfigure.security.oauth.ResourceServerAutoConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Collections;

/**
 * 跨域配置
 *
 * @author CloudS3n
 * @date 2021-06-17 17:58
 */
@Slf4j
@Configuration
@ConditionalOnClass(CorsConfigurationSource.class)
@AutoConfigureBefore({DefaultWebSecurityAutoConfiguration.class, ResourceServerAutoConfiguration.class})
public class CorsAutoConfiguration {

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Collections.singletonList("*"));
        configuration.setAllowedMethods(Collections.singletonList("*"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
