package com.paygoal.exercie.service;

import com.paygoal.exercie.dto.ProductDto;
import com.paygoal.exercie.exception.ProductNotFoundException;
import com.paygoal.exercie.model.Product;
import com.paygoal.exercie.repository.ProductRepository;
import com.paygoal.exercie.utils.ProductMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductMapper productMapper;

    @InjectMocks
    private ProductServiceImpl productService;

    private Product testProduct;
    private ProductDto testProductDto;
    private List<Product> productList;
    private List<ProductDto> productDtoList;

    @BeforeEach
    void setUp() {
        testProduct = Product.builder()
                .id(1L)
                .name("Test Product")
                .description("Test Description")
                .price(new BigDecimal("99.99"))
                .stock(10)
                .build();

        testProductDto = ProductDto.builder()
                .id(1L)
                .name("Test Product")
                .description("Test Description")
                .price(new BigDecimal("99.99"))
                .stock(10)
                .build();

        Product product1 = Product.builder()
                .id(1L)
                .name("Product 1")
                .description("Description 1")
                .price(new BigDecimal("19.99"))
                .stock(5)
                .build();

        Product product2 = Product.builder()
                .id(2L)
                .name("Product 2")
                .description("Description 2")
                .price(new BigDecimal("29.99"))
                .stock(10)
                .build();

        productList = Arrays.asList(product1, product2);

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
    void findByIdShouldReturnProductDtoWhenProductExists() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));
        when(productMapper.toDto(testProduct)).thenReturn(testProductDto);

        ProductDto found = productService.findById(1L);

        assertNotNull(found);
        assertEquals(1L, found.getId());
        assertEquals("Test Product", found.getName());
        verify(productRepository, times(1)).findById(1L);
        verify(productMapper, times(1)).toDto(testProduct);
    }

    @Test
    void findByIdShouldThrowExceptionWhenProductDoesNotExist() {
        when(productRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class, () -> {
            productService.findById(99L);
        });

        verify(productRepository, times(1)).findById(99L);
        verify(productMapper, never()).toDto(any(Product.class));
    }

    @Test
    void createShouldSaveAndReturnProductDto() {
        ProductDto newProductDto = ProductDto.builder()
                .name("New Product")
                .description("New Description")
                .price(new BigDecimal("59.99"))
                .stock(7)
                .build();

        Product newProduct = Product.builder()
                .name("New Product")
                .description("New Description")
                .price(new BigDecimal("59.99"))
                .stock(7)
                .build();

        when(productMapper.toEntity(newProductDto)).thenReturn(newProduct);
        when(productRepository.save(newProduct)).thenReturn(testProduct);
        when(productMapper.toDto(testProduct)).thenReturn(testProductDto);

        ProductDto saved = productService.create(newProductDto);

        assertNotNull(saved);
        assertEquals(1L, saved.getId());
        assertEquals("Test Product", saved.getName());
        verify(productMapper, times(1)).toEntity(newProductDto);
        verify(productRepository, times(1)).save(newProduct);
        verify(productMapper, times(1)).toDto(testProduct);
    }

    @Test
    void updateShouldUpdateAndReturnProductDtoWhenProductExists() {
        ProductDto updateDetailsDto = ProductDto.builder()
                .name("Updated Product")
                .description("Updated Description")
                .price(new BigDecimal("79.99"))
                .stock(15)
                .build();

        Product updatedProduct = Product.builder()
                .id(1L)
                .name("Updated Product")
                .description("Updated Description")
                .price(new BigDecimal("79.99"))
                .stock(15)
                .build();

        ProductDto updatedProductDto = ProductDto.builder()
                .id(1L)
                .name("Updated Product")
                .description("Updated Description")
                .price(new BigDecimal("79.99"))
                .stock(15)
                .build();

        when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));
        doNothing().when(productMapper).updateProductFromDto(updateDetailsDto, testProduct);
        when(productRepository.save(testProduct)).thenReturn(updatedProduct);
        when(productMapper.toDto(updatedProduct)).thenReturn(updatedProductDto);

        ProductDto updated = productService.update(1L, updateDetailsDto);

        assertNotNull(updated);
        assertEquals(1L, updated.getId());
        assertEquals("Updated Product", updated.getName());
        assertEquals("Updated Description", updated.getDescription());
        assertEquals(0, new BigDecimal("79.99").compareTo(updated.getPrice()));
        assertEquals(15, updated.getStock());

        verify(productRepository, times(1)).findById(1L);
        verify(productMapper, times(1)).updateProductFromDto(updateDetailsDto, testProduct);
        verify(productRepository, times(1)).save(testProduct);
        verify(productMapper, times(1)).toDto(updatedProduct);
    }

    @Test
    void updateShouldThrowExceptionWhenProductDoesNotExist() {
        ProductDto updateDetailsDto = ProductDto.builder()
                .name("Updated Product")
                .description("Updated Description")
                .price(new BigDecimal("79.99"))
                .stock(15)
                .build();

        when(productRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class, () -> {
            productService.update(99L, updateDetailsDto);
        });

        verify(productRepository, times(1)).findById(99L);
        verify(productMapper, never()).updateProductFromDto(any(), any());
        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    void deleteShouldDeleteProductWhenProductExists() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));
        doNothing().when(productRepository).delete(any(Product.class));

        productService.delete(1L);

        verify(productRepository, times(1)).findById(1L);
        verify(productRepository, times(1)).delete(testProduct);
    }

    @Test
    void deleteShouldThrowExceptionWhenProductDoesNotExist() {
        when(productRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class, () -> {
            productService.delete(99L);
        });

        verify(productRepository, times(1)).findById(99L);
        verify(productRepository, never()).delete(any(Product.class));
    }

    @Test
    void findAllOrderedByPriceShouldReturnSortedProductDtos() {
        when(productRepository.findAll(any(Sort.class))).thenReturn(productList);
        when(productMapper.toDtoList(productList)).thenReturn(productDtoList);

        List<ProductDto> result = productService.findAllOrderedByPrice();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Product 1", result.get(0).getName());

        verify(productRepository, times(1)).findAll(any(Sort.class));
        verify(productMapper, times(1)).toDtoList(productList);
    }
}