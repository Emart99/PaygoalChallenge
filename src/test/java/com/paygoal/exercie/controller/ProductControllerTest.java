package com.paygoal.exercie.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.paygoal.exercie.dto.ProductDto;
import com.paygoal.exercie.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @Autowired
    private ObjectMapper objectMapper;

    private ProductDto testProductDto;
    private List<ProductDto> productDtoList;

    @BeforeEach
    void setUp() {
        testProductDto = ProductDto.builder()
                .id(1L)
                .name("Test Product")
                .description("Test Description")
                .price(new BigDecimal("99.99"))
                .stock(10)
                .build();

        ProductDto productDto1 = ProductDto.builder()
                .id(1L)
                .name("Product 1")
                .description("Description 1")
                .price(new BigDecimal("19.99"))
                .stock(5)
                .build();

        ProductDto productDto2 = ProductDto.builder()
                .id(2L)
                .name("Product 2")
                .description("Description 2")
                .price(new BigDecimal("29.99"))
                .stock(10)
                .build();

        productDtoList = Arrays.asList(productDto1, productDto2);
    }

    @Test
    void findByIdShouldReturnProduct() throws Exception {
        when(productService.findById(1L)).thenReturn(testProductDto);

        mockMvc.perform(get("/api/products/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Test Product"))
                .andExpect(jsonPath("$.description").value("Test Description"))
                .andExpect(jsonPath("$.price").value(99.99))
                .andExpect(jsonPath("$.stock").value(10));

        verify(productService, times(1)).findById(1L);
    }

    @Test
    void createProductShouldCreateAndReturnProduct() throws Exception {
        ProductDto inputProductDto = ProductDto.builder()
                .name("New Product")
                .description("New Description")
                .price(new BigDecimal("59.99"))
                .stock(7)
                .build();

        when(productService.create(any(ProductDto.class))).thenReturn(testProductDto);

        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inputProductDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Test Product"));

        verify(productService, times(1)).create(any(ProductDto.class));
    }

    @Test
    void updateShouldUpdateAndReturnProduct() throws Exception {
        ProductDto updatedProductDto = ProductDto.builder()
                .id(1L)
                .name("Updated Product")
                .description("Updated Description")
                .price(new BigDecimal("79.99"))
                .stock(15)
                .build();

        when(productService.update(eq(1L), any(ProductDto.class))).thenReturn(updatedProductDto);

        mockMvc.perform(put("/api/products/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedProductDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Product"))
                .andExpect(jsonPath("$.price").value(79.99));

        verify(productService, times(1)).update(eq(1L), any(ProductDto.class));
    }

    @Test
    void deleteShouldReturnNoContent() throws Exception {
        doNothing().when(productService).delete(1L);

        mockMvc.perform(delete("/api/products/1"))
                .andExpect(status().isNoContent());

        verify(productService, times(1)).delete(1L);
    }

    @Test
    void findAllOrderedByPriceShouldReturnSortedProducts() throws Exception {
        when(productService.findAllOrderedByPrice()).thenReturn(productDtoList);

        mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Product 1"))
                .andExpect(jsonPath("$[1].name").value("Product 2"));

        verify(productService, times(1)).findAllOrderedByPrice();
    }

    @Test
    void addWithInvalidDataShouldReturnBadRequest() throws Exception {
        ProductDto invalidProductDto = ProductDto.builder()
                .name("")
                .description("Description")
                .price(new BigDecimal("-10.00"))
                .stock(-5)
                .build();

        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidProductDto)))
                .andExpect(status().isBadRequest());

        verify(productService, never()).create(any(ProductDto.class));
    }
}