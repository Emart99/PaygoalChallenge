package com.paygoal.exercie.service;
import com.paygoal.exercie.exception.ProductNotFoundException;
import com.paygoal.exercie.model.Product;
import com.paygoal.exercie.repository.ProductRepository;
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

    @InjectMocks
    private ProductServiceImpl productService;

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
    void findByIdShouldReturnProductWhenProductExists() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));

        Product found = productService.findById(1L);

        assertNotNull(found);
        assertEquals(1L, found.getId());
        assertEquals("Test Product", found.getName());
        verify(productRepository, times(1)).findById(1L);
    }

    @Test
    void findByIdShouldThrowExceptionWhenProductDoesNotExist() {
        when(productRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class, () -> {
            productService.findById(99L);
        });

        verify(productRepository, times(1)).findById(99L);
    }

    @Test
    void addShouldSaveAndReturnProduct() {
        Product newProduct = new Product("New Product", "New Description", new BigDecimal("59.99"), 7);

        when(productRepository.save(any(Product.class))).thenReturn(testProduct);

        Product saved = productService.add(newProduct);

        assertNotNull(saved);
        assertEquals(1L, saved.getId());
        assertEquals("Test Product", saved.getName());
        verify(productRepository, times(1)).save(newProduct);
    }

    @Test
    void updateShouldUpdateAndReturnProductWhenProductExists() {
        Product updateDetails = new Product("Updated Product", "Updated Description", new BigDecimal("79.99"), 15);

        when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));
        when(productRepository.save(any(Product.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Product updated = productService.update(1L, updateDetails);

        assertNotNull(updated);
        assertEquals(1L, updated.getId());
        assertEquals("Updated Product", updated.getName());
        assertEquals("Updated Description", updated.getDescription());
        assertEquals(new BigDecimal("79.99"), updated.getPrice());
        assertEquals(15, updated.getStock());

        verify(productRepository, times(1)).findById(1L);
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    void updateShouldThrowExceptionWhenProductDoesNotExist() {
        Product updateDetails = new Product("Updated Product", "Updated Description", new BigDecimal("79.99"), 15);

        when(productRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class, () -> {
            productService.update(99L, updateDetails);
        });

        verify(productRepository, times(1)).findById(99L);
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
    void delete_ShouldThrowException_WhenProductDoesNotExist() {
        when(productRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class, () -> {
            productService.delete(99L);
        });

        verify(productRepository, times(1)).findById(99L);
        verify(productRepository, never()).delete(any(Product.class));
    }

    @Test
    void findAllOrderedByPrice_ShouldReturnSortedProducts() {
        when(productRepository.findAll(any(Sort.class))).thenReturn(productList);

        Iterable<Product> result = productService.findAllOrderedByPrice();

        assertNotNull(result);
        List<Product> resultList = (List<Product>) result;
        assertEquals(2, resultList.size());
        assertEquals("Product 1", resultList.get(0).getName());

        verify(productRepository, times(1)).findAll(any(Sort.class));
    }
}
