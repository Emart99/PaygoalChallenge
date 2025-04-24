package com.paygoal.exercie.model;


import org.junit.jupiter.api.Test;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

public class ProductTest {

    private final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    private final Validator validator = factory.getValidator();

    @Test
    void testProductConstructorAndGetters() {
        String name = "Test Product";
        String description = "Test Description";
        BigDecimal price = new BigDecimal("99.99");
        int stock = 10;

        Product product = new Product(name, description, price, stock);
        product.setId(1L);

        assertEquals(1L, product.getId());
        assertEquals(name, product.getName());
        assertEquals(description, product.getDescription());
        assertEquals(price, product.getPrice());
        assertEquals(stock, product.getStock());
    }

    @Test
    void testProductSetters() {
        Product product = new Product();

        product.setId(2L);
        product.setName("Updated Product");
        product.setDescription("Updated Description");
        product.setPrice(new BigDecimal("199.99"));
        product.setStock(20);

        assertEquals(2L, product.getId());
        assertEquals("Updated Product", product.getName());
        assertEquals("Updated Description", product.getDescription());
        assertEquals(new BigDecimal("199.99"), product.getPrice());
        assertEquals(20, product.getStock());
    }

    @Test
    void testValidProduct() {
        Product product = new Product("Valid Product", "Valid Description", new BigDecimal("50.00"), 5);

        var violations = validator.validate(product);

        assertTrue(violations.isEmpty());
    }

    @Test
    void testInvalidProductWithBlankName() {
        Product product = new Product("", "Description", new BigDecimal("50.00"), 5);

        var violations = validator.validate(product);

        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertEquals("Name is required", violations.iterator().next().getMessage());
    }

    @Test
    void testInvalidProductWithNullName() {
        Product product = new Product(null, "Description", new BigDecimal("50.00"), 5);

        var violations = validator.validate(product);

        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertEquals("Name is required", violations.iterator().next().getMessage());
    }

    @Test
    void testInvalidProductWithNullPrice() {
        Product product = new Product("Product", "Description", null, 5);

        var violations = validator.validate(product);

        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertEquals("Price is required", violations.iterator().next().getMessage());
    }

    @Test
    void testInvalidProductWithNegativePrice() {
        Product product = new Product("Product", "Description", new BigDecimal("-10.00"), 5);

        var violations = validator.validate(product);

        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertEquals("Price must be greater than 0", violations.iterator().next().getMessage());
    }

    @Test
    void testInvalidProductWithNegativeStock() {
        Product product = new Product("Product", "Description", new BigDecimal("50.00"), -5);

        var violations = validator.validate(product);

        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertEquals("Stock cannot be negative", violations.iterator().next().getMessage());
    }
}