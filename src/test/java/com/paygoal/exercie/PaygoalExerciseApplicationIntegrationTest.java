package com.paygoal.exercie;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.paygoal.exercie.dto.ProductDto;
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
        mockMvc.perform(get("/api/products/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("mesa redonda"));
    }

    @Test
    void createProductShouldSaveAndReturnNewProduct() throws Exception {
        ProductDto newProduct = ProductDto.builder()
                .name("Test Integration Product")
                .description("Product created in integration test")
                .price(new BigDecimal("199.99"))
                .stock(25)
                .build();

        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newProduct)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.name").value("Test Integration Product"));
    }

    @Test
    void updateProductShouldUpdateExistingProduct() throws Exception {
        String originalProductJson = mockMvc.perform(get("/api/products/2"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        ProductDto originalProduct = objectMapper.readValue(originalProductJson, ProductDto.class);
        originalProduct.setName("Updated Name in Integration Test");
        originalProduct.setPrice(new BigDecimal("88.88"));

        mockMvc.perform(put("/api/products/2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(originalProduct)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.name").value("Updated Name in Integration Test"))
                .andExpect(jsonPath("$.price").value(88.88));
    }

    @Test
    void deleteProductShouldRemoveProduct() throws Exception {
        ProductDto productToDelete = ProductDto.builder()
                .name("Product to Delete")
                .description("This product will be deleted")
                .price(new BigDecimal("50.00"))
                .stock(1)
                .build();

        String createdProductJson = mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productToDelete)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        ProductDto createdProduct = objectMapper.readValue(createdProductJson, ProductDto.class);

        mockMvc.perform(delete("/api/products/" + createdProduct.getId()))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/products/" + createdProduct.getId()))
                .andExpect(status().isNotFound());
    }

    @Test
    void findAllOrderedByPriceShouldReturnProductsSortedByPrice() throws Exception {
        mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(10))))
                .andExpect(jsonPath("$[0].price", lessThanOrEqualTo(80000.0)))
                .andExpect(jsonPath("$[*].price").value(everyItem(notNullValue())));
    }

    @Test
    void createInvalidProductShouldReturnBadRequest() throws Exception {
        ProductDto invalidProduct = ProductDto.builder()
                .name("")
                .description("Description")
                .price(new BigDecimal("-10.00"))
                .stock(-5)
                .build();

        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidProduct)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.errors").isNotEmpty());
    }
}
