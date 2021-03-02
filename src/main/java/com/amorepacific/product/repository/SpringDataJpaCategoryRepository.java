package com.amorepacific.product.repository;

import com.amorepacific.product.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface SpringDataJpaCategoryRepository extends CrudRepository<Category, Integer>, JpaRepository<Category, Integer>, CategoryRepository {

}
