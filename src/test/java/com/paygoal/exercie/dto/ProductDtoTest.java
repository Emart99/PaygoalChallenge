package com.paygoal.exercie.dto;
import org.junit.jupiter.api.Test;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

public class ProductDtoTest {

    private final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    private final Validator validator = factory.getValidator();

    @Test
    void testProductDtoBuilderAndGetters() {
        String name = "Test Product";
        String description = "Test Description";
        BigDecimal price = new BigDecimal("99.99");
        int stock = 10;

        ProductDto productDto = ProductDto.builder()
                .id(1L)
                .name(name)
                .description(description)
                .price(price)
                .stock(stock)
                .build();

        assertEquals(1L, productDto.getId());
        assertEquals(name, productDto.getName());
        assertEquals(description, productDto.getDescription());
        assertEquals(price, productDto.getPrice());
        assertEquals(stock, productDto.getStock());
    }

    @Test
    void testProductDtoSetters() {
        ProductDto productDto = new ProductDto();

        productDto.setId(2L);
        productDto.setName("Updated Product");
        productDto.setDescription("Updated Description");
        productDto.setPrice(new BigDecimal("199.99"));
        productDto.setStock(20);

        assertEquals(2L, productDto.getId());
        assertEquals("Updated Product", productDto.getName());
        assertEquals("Updated Description", productDto.getDescription());
        assertEquals(new BigDecimal("199.99"), productDto.getPrice());
        assertEquals(20, productDto.getStock());
    }

    @Test
    void testValidProductDto() {
        ProductDto productDto = ProductDto.builder()
                .name("Valid Product")
                .description("Valid Description")
                .price(new BigDecimal("50.00"))
                .stock(5)
                .build();

        var violations = validator.validate(productDto);

        assertTrue(violations.isEmpty());
    }

    @Test
    void testInvalidProductDtoWithBlankName() {
        ProductDto productDto = ProductDto.builder()
                .name("")
                .description("Description")
                .price(new BigDecimal("50.00"))
                .stock(5)
                .build();

        var violations = validator.validate(productDto);

        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertEquals("Name is required", violations.iterator().next().getMessage());
    }

    @Test
    void testInvalidProductDtoWithNullName() {
        ProductDto productDto = ProductDto.builder()
                .name(null)
                .description("Description")
                .price(new BigDecimal("50.00"))
                .stock(5)
                .build();

        var violations = validator.validate(productDto);

        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertEquals("Name is required", violations.iterator().next().getMessage());
    }

    @Test
    void testInvalidProductDtoWithNullPrice() {
        ProductDto productDto = ProductDto.builder()
                .name("Product")
                .description("Description")
                .price(null)
                .stock(5)
                .build();

        var violations = validator.validate(productDto);

        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertEquals("Price is required", violations.iterator().next().getMessage());
    }

    @Test
    void testInvalidProductDtoWithNegativePrice() {
        ProductDto productDto = ProductDto.builder()
                .name("Product")
                .description("Description")
                .price(new BigDecimal("-10.00"))
                .stock(5)
                .build();

        var violations = validator.validate(productDto);

        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertEquals("Price must be greater than 0", violations.iterator().next().getMessage());
    }

    @Test
    void testInvalidProductDtoWithNegativeStock() {
        ProductDto productDto = ProductDto.builder()
                .name("Product")
                .description("Description")
                .price(new BigDecimal("50.00"))
                .stock(-5)
                .build();

        var violations = validator.validate(productDto);

        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertEquals("Stock cannot be negative", violations.iterator().next().getMessage());
    }
}
