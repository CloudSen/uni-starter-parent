package cn.unic.starter.autoconfigure.security;

import cn.unic.starter.autoconfigure.security.properties.SpringSecurityProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
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
@ConditionalOnProperty(name = "unic.config.starter.enable-security", havingValue = "true")
@ConditionalOnBean(WebSecurityConfigurerAdapter.class)
public class DefaultWebSecurityAutoConfiguration extends WebSecurityConfigurerAdapter {

    private final SpringSecurityProperties springSecurityProperties;

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers(springSecurityProperties.getIgnoringAntMatchers().toArray(new String[]{}));
    }
}
