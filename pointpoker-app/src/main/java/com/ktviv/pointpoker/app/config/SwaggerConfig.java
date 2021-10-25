package com.ktviv.pointpoker.app.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Collections;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2).select()
                .apis(RequestHandlerSelectors.basePackage("com.ktviv.pointpoker.app"))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(apiInfo());
    }

    private ApiInfo apiInfo() {
        ApiInfo apiInfo = new ApiInfo(
                "PointPoker App API",
                "REST endpoints for PointPoker App ",
                "v1.0.0",
                "PointPoker App API: Terms of service",
                new Contact("Vivek K T", "https://vivekkt.dev", "vivekkt89@gmail.com"),
                "GNU GENERAL PUBLIC LICENSE",
                "https://raw.githubusercontent.com/ktviv/pointpoker/main/LICENSE",
                Collections.emptyList());
        return apiInfo;
    }
}
