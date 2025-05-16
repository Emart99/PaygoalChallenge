package com.paygoal.exercie.controller;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.paygoal.exercie.model.Product;
import com.paygoal.exercie.service.ProductServiceImpl;
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
    private ProductServiceImpl productService;

    @Autowired
    private ObjectMapper objectMapper;

    private Product testProduct;
    private List<Product> productList;

    @BeforeEach
    void setUp() {
        testProduct = new Product("Test Product", "Test Description", new BigDecimal("99.99"), 10);
        testProduct.setId(1L);
        Product product1 = new Product("Product 1", "Description 1", new BigDecimal("19.99"), 5);
        product1.setId(1L);
        Product product2 = new Product("Product 2", "Description 2", new BigDecimal("29.99"), 10);
        product2.setId(2L);

        productList = Arrays.asList(product1, product2);
    }

    @Test
    void findByIdShouldReturnProduct() throws Exception {
        when(productService.findById(1L)).thenReturn(testProduct);

        mockMvc.perform(get("/api/products/getProductById/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Test Product"))
                .andExpect(jsonPath("$.description").value("Test Description"))
                .andExpect(jsonPath("$.price").value(99.99))
                .andExpect(jsonPath("$.stock").value(10));
        verify(productService, times(1)).findById(1L);
    }

    @Test
    void addShouldCreateAndReturnProduct() throws Exception {
        Product inputProduct = new Product("New Product", "New Description", new BigDecimal("59.99"), 7);

        when(productService.add(any(Product.class))).thenReturn(testProduct);

        mockMvc.perform(post("/api/products/createProduct/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inputProduct)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Test Product"));

        verify(productService, times(1)).add(any(Product.class));
    }

    @Test
    void updateShouldUpdateAndReturnProduct() throws Exception {
        Product updatedProduct = new Product("Updated Product", "Updated Description", new BigDecimal("79.99"), 15);
        updatedProduct.setPrice(new BigDecimal("90009.99"));

        when(productService.update(eq(1L), any(Product.class))).thenReturn(updatedProduct);

        mockMvc.perform(put("/api/products/updateProduct/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedProduct)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Product"))
                .andExpect(jsonPath("$.price").value(90009.99));

        verify(productService, times(1)).update(eq(1L), any(Product.class));
    }

    @Test
    void deleteShouldReturnNoContent() throws Exception {
        doNothing().when(productService).delete(1L);

        mockMvc.perform(delete("/api/products/deleteProduct/1"))
                .andExpect(status().isNoContent());

        verify(productService, times(1)).delete(1L);
    }

    @Test
    void findAllOrderedByPriceShouldReturnSortedProducts() throws Exception {
        when(productService.findAllOrderedByPrice()).thenReturn(productList);

        mockMvc.perform(get("/api/products/findAllOrderedByPrice"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Product 1"))
                .andExpect(jsonPath("$[1].name").value("Product 2"));

        verify(productService, times(1)).findAllOrderedByPrice();
    }

    @Test
    void addWithInvalidDataShouldReturnBadRequest() throws Exception {
        Product invalidProduct = new Product("", null, new BigDecimal("-10.0"), -5);

        mockMvc.perform(post("/api/products/createProduct/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidProduct)))
                .andExpect(status().isBadRequest());

        verify(productService, never()).add(any(Product.class));
    }
}
