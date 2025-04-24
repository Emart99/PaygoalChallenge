package com.paygoal.exercie.exception;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

public class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler exceptionHandler;

    @BeforeEach
    void setUp() {
        exceptionHandler = new GlobalExceptionHandler();
    }

    @Test
    void handleProductNotFoundExceptionShouldReturnNotFoundStatus() {
        ProductNotFoundException ex = new ProductNotFoundException("Product not found");

        ResponseEntity<?> response = exceptionHandler.handleProductNotFoundException(ex);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

        Map<String, Object> body = (Map<String, Object>) response.getBody();
        assertEquals(404, body.get("status"));
        assertEquals("Product not found", body.get("message"));
        assertNotNull(body.get("timestamp"));
    }

    @Test
    void handleBadRequestExceptionShouldReturnBadRequestStatus() {
        BadRequestException ex = new BadRequestException("Invalid request");

        ResponseEntity<?> response = exceptionHandler.handleBadRequestException(ex);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

        Map<String, Object> body = (Map<String, Object>) response.getBody();
        assertEquals(400, body.get("status"));
        assertEquals("Invalid request", body.get("message"));
        assertNotNull(body.get("timestamp"));
    }

    @Test
    void handleGlobalExceptionShouldReturnInternalServerError() {
        Exception ex = new Exception("Something went wrong");

        ResponseEntity<?> response = exceptionHandler.handleGlobalException(ex);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());

        Map<String, Object> body = (Map<String, Object>) response.getBody();
        assertEquals(500, body.get("status"));
        assertEquals("Internal server error", body.get("message"));
        assertNotNull(body.get("timestamp"));
    }
}
