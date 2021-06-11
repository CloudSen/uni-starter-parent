package cn.unic.starter.autoconfigure.es;

import cn.unic.starter.autoconfigure.AutoConfigConstants;
import cn.unic.starter.autoconfigure.es.properties.EsProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.sniff.Sniffer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author CloudS3n
 * @date 2021-06-11 09:58
 */
@Slf4j
@Configuration
@EnableConfigurationProperties({EsProperties.class})
@ConditionalOnClass(RestHighLevelClient.class)
@ConditionalOnProperty(name = "unic.config.starter.enable-es", havingValue = "true")
public class EsAutoConfiguration {

    public EsAutoConfiguration() {
        log.info(AutoConfigConstants.LOADING_ES_AUTO_CONFIGURE);
    }

    private static InetAddress parseAddress(String s) {
        try {
            return InetAddress.getByName(s);
        } catch (UnknownHostException e) {
            log.error(ExceptionUtils.getStackTrace(e));
        }
        return null;
    }

    @Bean
    @ConditionalOnMissingBean
    public RestHighLevelClient esClient(EsProperties properties) {
        return new RestHighLevelClient(
            RestClient.builder(
                properties.getAddresses().stream()
                    .map(EsAutoConfiguration::parseAddress).filter(Objects::nonNull)
                    .map(address -> new HttpHost(address, properties.getRestPort()))
                    .collect(Collectors.toList())
                    .toArray(new HttpHost[properties.getAddresses().size()])
            )
        );
    }

    @Bean
    @ConditionalOnProperty(name = "unic.config.es.enable-sniff", havingValue = "true")
    @ConditionalOnMissingBean
    public Sniffer sniffer(RestHighLevelClient esClient) {
        return Sniffer.builder(esClient.getLowLevelClient()).build();
    }
}
