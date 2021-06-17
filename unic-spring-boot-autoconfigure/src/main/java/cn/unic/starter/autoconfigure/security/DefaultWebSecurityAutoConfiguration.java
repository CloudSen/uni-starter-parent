package cn.unic.starter.autoconfigure.security;

import cn.unic.starter.autoconfigure.AutoConfigConstants;
import cn.unic.starter.autoconfigure.security.properties.SpringSecurityProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * @author CloudS3n
 * @date 2021-06-15 13:59
 */
@Configuration
@SuppressWarnings("RedundantThrows")
@RequiredArgsConstructor
@EnableConfigurationProperties(SpringSecurityProperties.class)
@ConditionalOnProperty(name = AutoConfigConstants.UNIC_DEFAULT_CONFIG_SECURITY, havingValue = AutoConfigConstants.TRUE)
@ConditionalOnClass(SecurityAutoConfiguration.class)
@AutoConfigureBefore(SecurityAutoConfiguration.class)
public class DefaultWebSecurityAutoConfiguration extends WebSecurityConfigurerAdapter {

    private final SpringSecurityProperties springSecurityProperties;

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.csrf().disable().cors().and()
            .authorizeRequests().anyRequest().permitAll();
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers(springSecurityProperties.getIgnoringAntMatchers().toArray(new String[]{}));
    }
}
