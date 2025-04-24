package com.paygoal.exercie.repository;

import com.paygoal.exercie.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
