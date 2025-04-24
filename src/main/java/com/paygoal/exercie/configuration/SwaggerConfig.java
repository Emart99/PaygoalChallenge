package com.paygoal.exercie.configuration;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI productOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Ejercicio Jr")
                        .description("Api rest Paygoal challenge")
                        .version("v1.0.0"))
                        .externalDocs(new ExternalDocumentation()
                        .description("Product Management Documentation")
                        .url("https://github.com/Emart99/PaygoalEx"))
                        .servers(Arrays.asList(
                        new Server().url("http://localhost:8080").description("Development Server")
                ));
    }
}
