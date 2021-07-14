package cn.uni.starter.autoconfigure.swagger;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import springfox.documentation.RequestHandler;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

/**
 * Swagger辅助配置接口
 *
 * @author CloudS3n
 * @date 2021-07-14 15:36
 */
public interface SwaggerConfigure {

    Map<String, Contact> CONTACT_MAP = new HashMap<>(16);
    List<SecurityScheme> SECURITY_SCHEMAS = Collections.singletonList(
        HttpAuthenticationScheme.JWT_BEARER_BUILDER
            .name("JWT")
            .build()
    );
    List<SecurityContext> SECURITY_CONTEXTS = Collections.singletonList(
        SecurityContext.builder()
            .securityReferences(Collections.singletonList(SecurityReference.builder()
                .scopes(new AuthorizationScope[0])
                .reference("JWT")
                .build()))
            .operationSelector(o -> o.requestMappingPattern().matches("/.*"))
            .build()
    );

    /**
     * 创建Docket对象
     *
     * @param property docket属性
     * @return Docket对象
     */
    default Docket buildDocket(DocketProperty property) {
        return new Docket(DocumentationType.OAS_30)
            .groupName(property.getGroupName())
            .apiInfo(
                new ApiInfoBuilder()
                    .title(property.getTitle())
                    .contact(CONTACT_MAP.get(property.getContact()))
                    .description(property.getDescription())
                    .version(property.getVersion())
                    .build()
            )
            .securitySchemes(SECURITY_SCHEMAS)
            .securityContexts(SECURITY_CONTEXTS)
            .select()
            .apis(property.getApis())
            .paths(property.getPaths())
            .build();
    }

    @Data
    @Accessors(chain = true)
    @NoArgsConstructor
    @AllArgsConstructor
    class DocketProperty {
        private String groupName;
        private String title;
        private String contact;
        private String description;
        private String version;
        private Predicate<RequestHandler> apis;
        private Predicate<String> paths;
    }
}
