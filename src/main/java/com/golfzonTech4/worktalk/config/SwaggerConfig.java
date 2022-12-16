package com.golfzonTech4.worktalk.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
@SuppressWarnings("unchecked")
@OpenAPIDefinition(
        info = @Info(title = "WorkTalk Reset API 명세서",
                description = "사무 공간 대여 플랫폼 프로젝트",
                version = "v1"))
@RequiredArgsConstructor
@Configuration
public class SwaggerConfig {

    @Bean
    public GroupedOpenApi chatOpenApi() {
        String[] paths = {"/v1/**"};

        return GroupedOpenApi.builder()
                .group("WorkTalk API v1")
                .pathsToMatch(paths)
                .build();
    }

}
