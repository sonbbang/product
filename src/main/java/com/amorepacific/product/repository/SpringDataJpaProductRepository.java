package com.amorepacific.product.repository;

import com.amorepacific.product.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface SpringDataJpaProductRepository extends CrudRepository<Product, Long>, JpaRepository<Product, Long>, ProductRepository {

    @Override
    List<Product> findByCategoryNo(Integer categoryNo);

}
