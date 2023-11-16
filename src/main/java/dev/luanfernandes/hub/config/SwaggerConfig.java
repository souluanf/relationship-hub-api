package dev.luanfernandes.hub.config;

import static java.util.stream.Stream.of;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
        info =
                @Info(
                        title = "relashionship-hub-api",
                        version = "0.0.1",
                        description = "API for relationship-hub project",
                        contact =
                                @io.swagger.v3.oas.annotations.info.Contact(
                                        name = "Luan Fernandes",
                                        email = "hello@luanfernandes.dev",
                                        url = "https://luanfernandes.dev")),
        externalDocs =
                @ExternalDocumentation(
                        description = "GitHub repository",
                        url = "https://github.com/souluanf/relationship-hub-api"))
@Configuration
public class SwaggerConfig {

    @Value("${swagger-servers-urls}")
    private String[] swaggerServersUrls;

    @Bean
    public OpenAPI customOpenAPI() {
        OpenAPI openApi = new OpenAPI();
        of(swaggerServersUrls).forEach(url -> openApi.addServersItem(new Server().url(url)));
        return openApi;
    }
}
