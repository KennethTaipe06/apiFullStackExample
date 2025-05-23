package com.FullStackExample.apiFullStackExample.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Library API - Book Management System")
                        .description("REST API for digital library management. " +
                                   "Supports CRUD operations on books, as well as advanced searches " +
                                   "by different criteria such as author, category, publication year, etc.")
                        .version("v1.0.0")
                        .contact(new Contact()
                                .name("Development Team")
                                .email("dev@library.com")
                                .url("https://github.com/your-username/library-api"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:8080")
                                .description("Development Server"),
                        new Server()
                                .url("https://api.library.com")
                                .description("Production Server")
                ));
    }
}
