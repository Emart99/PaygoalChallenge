package com.paygoal.exercie.service;

import com.paygoal.exercie.model.Product;

public interface ProductService {
    public Product findById(Long id);
    public Product add(Product product);
    public Product update(Long id, Product productDetails);
    public void delete(Long id);
    public Iterable<Product> findAllOrderedByPrice();
}
