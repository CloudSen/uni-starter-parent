package cn.unic.starter.autoconfigure.security.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotBlank;
import java.util.Arrays;
import java.util.List;

/**
 * @author CloudS3n
 * @date 2021-06-15 13:52
 */
@Data
@Component
@ConfigurationProperties(prefix = "unic.config.security")
public class SpringSecurityProperties {

    @NotBlank
    private String jwtKey = "uni-isse";

    private List<String> ignoringAntMatchers = Arrays.asList(
        "/index.html", "/static/**", "/login_p", "/druid/**", "/login/logout",
        "/v2/api-docs", "/swagger-resources/**", "/swagger-ui.html", "/webjars/**",
        "/announcementImg/**", "/error", "/pdfview/**", "/api/**"
    );
}
