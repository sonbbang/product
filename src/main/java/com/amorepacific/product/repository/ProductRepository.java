package com.amorepacific.product.repository;

import com.amorepacific.product.domain.Product;

import java.util.List;
import java.util.Optional;

public interface ProductRepository {
    List<Product> findByCategoryNo(Integer categoryNo);
    Optional<Product> findById(Long id);
    Product save(Product product);
    void deleteById(Long id);

}