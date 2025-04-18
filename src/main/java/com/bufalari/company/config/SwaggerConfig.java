package com.bufalari.company.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
public class SwaggerConfig {

    @Bean
    public Docket api() {
        // Substituído DocumentationType.SWAGGER_2 por DocumentationType.OAS_30 (OpenAPI 3) / Replaced Swagger 2 with OpenAPI 3 documentation
        return new Docket(DocumentationType.OAS_30)
                .select()
                // Substituído apis(RequestHandlerSelectors.any()) por filtro de pacote do controlador / Replaced apis(RequestHandlerSelectors.any()) with controller package filter
                .apis(RequestHandlerSelectors.basePackage("com.bufalari.company.controller"))
                .paths(PathSelectors.any())
                .build();
    }
}
