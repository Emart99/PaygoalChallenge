package com.paygoal.exercie.service;

import com.paygoal.exercie.exception.ProductNotFoundException;
import com.paygoal.exercie.model.Product;
import com.paygoal.exercie.repository.ProductRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Product findById(Long id) {
        return productRepository
                .findById(id)
                .orElseThrow(()->new ProductNotFoundException("Product not found"));
    }
    public Product add(Product product) {
        return productRepository.save(product);
    }
    public Product update(Long id, Product productDetails) {
        Product product = productRepository.findById(id)
                .orElseThrow(()->new ProductNotFoundException("Product not found"));
        product.setName(productDetails.getName());
        product.setDescription(productDetails.getDescription());
        product.setPrice(productDetails.getPrice());
        product.setStock(productDetails.getStock());
        return productRepository.save(product);
    }

    public void delete(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product not found"));
        productRepository.delete(product);
    }
    public Iterable<Product> findAllOrderedByPrice() {
        return productRepository.findAll(Sort.by("price"));
    }
}
