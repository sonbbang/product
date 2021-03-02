package com.amorepacific.product;

import com.amorepacific.product.repository.CategoryRepository;
import com.amorepacific.product.repository.ProductRepository;
import com.amorepacific.product.service.CacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringConfig {

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;

    @Autowired
    public SpringConfig(CategoryRepository categoryRepository, ProductRepository productRepository) {
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
    }

    @Bean
    public CacheService cacheService() {
        return new CacheService(categoryRepository, productRepository);
    }
}
