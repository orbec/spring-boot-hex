package com.bac.microservice.investmentfunds.rest.config;

import com.bac.utils.openapi.OpenApiServerConfig;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.models.OpenAPI;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "API - ${application.title}",
                description = "API para gestionar las transferencias internacionales",
                version = "${application.version}",
                contact = @Contact(
                        name = "BAC",
                        url = "https://www.baccredomatic.com/",
                        email = "contacto@bac.com"
                )))
@RequiredArgsConstructor
public class OpenApiConfig {

    private final OpenApiServerConfig openApiServerConfig;

    @Bean
    public OpenAPI customOpenApi() {
        return openApiServerConfig.getDefaultOpenApi();
    }

}
