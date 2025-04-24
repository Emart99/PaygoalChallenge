package com.paygoal.exercie;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.paygoal.exercie.model.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class PaygoalExerciseApplicationIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void contextLoads() {
    }

    @Test
    void findByIdShouldReturnProduct() throws Exception {
        mockMvc.perform(get("/api/products/getProductById/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("mesa redonda"));
    }

    @Test
    void createProductShouldSaveAndReturnNewProduct() throws Exception {
        Product newProduct = new Product(
                "Test Integration Product",
                "Product created in integration test",
                new BigDecimal("199.99"),
                25);

        mockMvc.perform(post("/api/products/createProduct/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newProduct)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.name").value("Test Integration Product"));
    }

    @Test
    void updateProductShouldUpdateExistingProduct() throws Exception {
        String originalProductJson = mockMvc.perform(get("/api/products/getProductById/2"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        Product originalProduct = objectMapper.readValue(originalProductJson, Product.class);
        originalProduct.setName("Updated Name in Integration Test");
        originalProduct.setPrice(new BigDecimal("88.88"));

        mockMvc.perform(put("/api/products/updateProduct/2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(originalProduct)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.name").value("Updated Name in Integration Test"))
                .andExpect(jsonPath("$.price").value(88.88));
    }

    @Test
    void deleteProductShouldRemoveProduct() throws Exception {
        Product productToDelete = new Product(
                "Product to Delete",
                "This product will be deleted",
                new BigDecimal("50.00"),
                1);

        String createdProductJson = mockMvc.perform(post("/api/products/createProduct/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productToDelete)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        Product createdProduct = objectMapper.readValue(createdProductJson, Product.class);

        mockMvc.perform(delete("/api/products/deleteProduct/" + createdProduct.getId()))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/products/getProductById/" + createdProduct.getId()))
                .andExpect(status().isNotFound());
    }

    @Test
    void findAllOrderedByPriceShouldReturnProductsSortedByPrice() throws Exception {
        mockMvc.perform(get("/api/products/findAllOrderedByPrice"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(equalTo(10))))
                .andExpect(jsonPath("$[0].price", lessThanOrEqualTo(80000.0)))
                .andExpect(jsonPath("$[*].price").value(everyItem(notNullValue())));
    }

    @Test
    void createInvalidProductShouldReturnBadRequest() throws Exception {
        Product invalidProduct = new Product(
                "",
                "Description",
                new BigDecimal("-10.00"),
                -5);

        mockMvc.perform(post("/api/products/createProduct/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidProduct)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.errors").isNotEmpty());
    }
}
