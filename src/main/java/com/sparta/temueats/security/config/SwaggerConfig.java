package com.sparta.temueats.security.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.Components;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    // OpenAPI 설정
    @Bean
    public OpenAPI openAPI() {
        String jwt = "7KCA64qU7Jik64qY7JWE66mU66as7Lm064W47JmA7LSI7L2U652865a866W866i57JeI7Iq164uI64uk7ZaJ67O17ZW07JqU";
        SecurityRequirement securityRequirement = new SecurityRequirement().addList(jwt);
        Components components = new Components().addSecuritySchemes(jwt, new SecurityScheme()
                .name(jwt)
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT")
        );

        return new OpenAPI()
                .components(components)
                .info(apiInfo())
                .addSecurityItem(securityRequirement);
    }

    // API 정보 설정
    private Info apiInfo() {
        return new Info()
                .title("temueats") // API의 제목
                .description("테무이츠 API 문서") // API에 대한 설명
                .version("1.0.0"); // API의 버전
    }
}
