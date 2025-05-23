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
                        .title("API Biblioteca - Sistema de Gestión de Libros")
                        .description("API REST para la gestión de una biblioteca digital. " +
                                   "Permite realizar operaciones CRUD sobre libros, así como búsquedas avanzadas " +
                                   "por diferentes criterios como autor, categoría, año de publicación, etc.")
                        .version("v1.0.0")
                        .contact(new Contact()
                                .name("Equipo de Desarrollo")
                                .email("dev@biblioteca.com")
                                .url("https://github.com/tu-usuario/biblioteca-api"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:8080")
                                .description("Servidor de Desarrollo"),
                        new Server()
                                .url("https://api.biblioteca.com")
                                .description("Servidor de Producción")
                ));
    }
}
