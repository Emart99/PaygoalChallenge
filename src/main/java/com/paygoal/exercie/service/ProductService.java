package com.paygoal.exercie.service;

import com.paygoal.exercie.dto.ProductDto;
import com.paygoal.exercie.model.Product;

import java.util.List;

public interface ProductService {
    ProductDto findById(Long id);
    ProductDto create(ProductDto productDto);
    ProductDto update(Long id, ProductDto productDto);
    void delete(Long id);
    List<ProductDto> findAllOrderedByPrice();
}
