package dev.challduck.portfolio.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
        info = @Info(title = "BBS API 명세서",
        description = "springboot BBS API 명세서",
        version = "v1")
)
@Configuration
public class SwaggerConfig {
    @Bean
    public GroupedOpenApi openApi() {
        String[] paths = {"/v1/**"};

        return GroupedOpenApi.builder()
                .group("BBS API v1")
                .pathsToMatch(paths)
                .build();
    }

}
