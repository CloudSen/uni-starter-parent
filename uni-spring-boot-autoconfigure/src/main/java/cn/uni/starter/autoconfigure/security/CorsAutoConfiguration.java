package cn.uni.starter.autoconfigure.security;

import cn.uni.starter.autoconfigure.security.oauth.ResourceServerAutoConfiguration;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
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
@Log4j2
@Configuration
@ConditionalOnBean(DefaultWebSecurityAutoConfiguration.class)
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
