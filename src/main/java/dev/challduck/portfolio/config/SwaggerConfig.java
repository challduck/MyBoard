package dev.challduck.portfolio.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.Collections;

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

    @Bean
    public OpenAPI openAPI(){

        String jwtSchemeName = "access_token";

        Components components = new Components().addSecuritySchemes(jwtSchemeName,
                new SecurityScheme()
                        .name(jwtSchemeName)
                        .in(SecurityScheme.In.HEADER).name("Authorization")
                        .type(SecurityScheme.Type.APIKEY).scheme("bearer").bearerFormat("JWT"));

        SecurityRequirement securityRequirement = new SecurityRequirement().addList(jwtSchemeName);

        return new OpenAPI()
                .info(new io.swagger.v3.oas.models.info.Info().title("springboot project")
                        .description("spring boot bbs 프로젝트 API 명세서")
                        .version("v0.0.1"))
                .components(components)
                .addSecurityItem(securityRequirement);
    }

}
