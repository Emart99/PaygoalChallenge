package com.paygoal.exercie.configuration;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.Map;

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
                        .url("https://github.com/Emart99/PaygoalChallenge"))
                .servers(Arrays.asList(
                        new Server().url("http://localhost:8080").description("Development Server")
                ))
                .components(new Components()
                        .addSchemas("NotFoundError", createSimpleErrorSchema(404, "Not Found"))
                        .addSchemas("BadRequestValidationError", createValidationErrorSchema())
                        .addResponses("NotFoundResponse", createApiResponse("Not Found", "#/components/schemas/NotFoundError"))
                        .addResponses("BadRequestResponse", createApiResponse("Bad Request", "#/components/schemas/BadRequestValidationError")));
    }

    private Schema<Object> createSimpleErrorSchema(int statusCode, String message) {
        Schema<Object> schema = new Schema<>();
        schema.setType("object");
        schema.addProperties("status", new Schema<>().type("integer").example(statusCode));
        schema.addProperties("message", new Schema<>().type("string").example(message));
        schema.addProperties("timestamp", new Schema<>().type("string").format("date-time").example("2025-04-24T12:34:56Z"));
        return schema;
    }

    private Schema<Object> createValidationErrorSchema() {
        Schema<Object> schema = new Schema<>();
        schema.setType("object");

        Schema<Object> errorsSchema = new Schema<>();
        errorsSchema.setType("object");
        errorsSchema.additionalProperties(new Schema<>().type("string"));
        errorsSchema.example(
                Map.of(
                        "price", "Price must be greater than 0",
                        "name", "Name is required",
                        "stock", "Stock cannot be negative"
                )
        );

        schema.addProperties("errors", errorsSchema);
        schema.addProperties("status", new Schema<>().type("integer").example(400));
        schema.addProperties("timestamp", new Schema<>().type("string").format("date-time").example("2025-04-24T14:58:20.149218"));
        return schema;
    }

    private ApiResponse createApiResponse(String description, String schemaRef) {
        return new ApiResponse()
                .description(description)
                .content(new Content().addMediaType("application/json",
                        new MediaType().schema(new Schema<>().$ref(schemaRef))));
    }
}
