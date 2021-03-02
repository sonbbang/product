package com.amorepacific.product.service;

import com.amorepacific.product.controller.dto.*;
import com.amorepacific.product.domain.Category;
import com.amorepacific.product.domain.Product;
import com.amorepacific.product.repository.CategoryRepository;
import com.amorepacific.product.repository.ProductRepository;
import com.amorepacific.product.service.lru.LRUCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CacheService {

    Logger logger = LoggerFactory.getLogger(CacheService.class);

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;

    // 카테고리 캐시
    private final Map<Integer, Category> categoryMap = new HashMap<>();
    // 카테고리별 상품 리스트 캐시
    private final Map<Integer, Map<Long, Product>> categoryProductMap = new HashMap<>();
    private final LRUCache lruCache = new LRUCache();

    private long categoryMapLoadTime;
    private long productMapLoadTime;

    // 캐시 유효시간 600초
    private final long categoryMapCacheDuration = 600 * 1000L;
    private final long productMapCacheDuration = 600 * 1000L;

    public CacheService(CategoryRepository categoryRepository, ProductRepository productRepository) {
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
    }

    // 초기화 메서드
    public void categoryCacheClear() {
        categoryMap.clear();
    }

    // 초기화 메서드
    public void productCacheClear() {
        categoryProductMap.clear();
    }

    // 카테고리 조회
    public List<CategoryDto> findCategories() {

        List<CategoryDto> list = new ArrayList<>();
        Map<Integer, Category> map = findCategoriesCache();

        for (Integer categoryNo : map.keySet()) {
            // 카테고리는 카테고리명이라는 단 1개의 속성만 갖는다.
            CategoryDto categoryDto = new CategoryDto();
            categoryDto.setCategoryName(map.get(categoryNo).getCategoryName());
            list.add(categoryDto);
        }
        return list;
    }

    public Map<Integer, Category> findCategoriesCache() {
        long now = System.currentTimeMillis();

        // 데이터가 적재되지 않았으면 데이터 저장소(DB)에서 데이터 가져오기
        if (categoryMap.isEmpty() || now - categoryMapLoadTime > categoryMapCacheDuration) {
            synchronized (categoryMap) {
                if (categoryMap.isEmpty() || now - categoryMapLoadTime > categoryMapCacheDuration) {

                    List<Category> categories = categoryRepository.findAll();
                    for (Category category : categories) {
                        // 카테고리명은 1차 카테고리명 - 2차 카테고리명으로 결합
                        if (category.getParentNo() != null) {
                            category.setCategoryName(categoryMap.get(category.getParentNo()).getCategoryName() + "-" + category.getCategoryName());
                        }
                        categoryMap.put(category.getCategoryNo(), category);
                    }
                    categoryMapLoadTime = now;
                }
            }
        }
        return categoryMap;
    }

    // 카테고리 등록
    public Category addCategory(CategoryAddForm categoryAddForm) {
        Category category = new Category();
        category.setCategoryName(categoryAddForm.getCategoryName());

        if (categoryAddForm.getParentNo() != null & categoryAddForm.getParentNo() > 0)
            category.setParentNo(categoryAddForm.getParentNo());

        category.setDepth(categoryAddForm.getDepth());
        return categoryRepository.save(category);
    }

    // 카테고리 조회
    public CategoryDto findCategory(Integer categoryNo) {
        Category category = categoryRepository.findById(categoryNo).orElseThrow(() -> new IllegalArgumentException("해당 카테고리가 존재하지 않습니다. categoryNo=" + categoryNo));
        String categoryName = category.getCategoryName();

        // 카테고리명은 1차 카테고리명 - 2차 카테고리명으로 결합
        if (category.getParentNo() != null) {
            Category parentCategory = categoryRepository.findById(category.getParentNo()).orElseThrow(() -> new IllegalArgumentException("부모 카테고리가 존재하지 않습니다. categoryNo=" + categoryNo));
            categoryName = parentCategory.getCategoryName() + "-" + categoryName;
        }
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setCategoryName(categoryName);
        return categoryDto;
    }

    // 카테고리 수정
    public Category updateCategory(Integer categoryNo, CategoryModifyForm categoryModifyForm) {
        Category category = categoryRepository.findById(categoryNo).orElseThrow(() -> new IllegalArgumentException("해당 카테고리가 존재하지 않습니다. categoryNo=" + categoryNo));

        if (categoryModifyForm.getCategoryName() != null) {
            category.setCategoryName(categoryModifyForm.getCategoryName());
        }
        return categoryRepository.save(category);
    }

    // 카테고리 삭제
    public void deleteCategory(Integer categoryNo) {
        categoryRepository.findById(categoryNo).orElseThrow(() -> new IllegalArgumentException("해당 카테고리가 존재하지 않습니다. categoryNo=" + categoryNo));
        categoryRepository.deleteById(categoryNo);
    }

    // 특정 카테고리 상품 리스트 조회
    public List<ProductDto> findProducts(Integer categoryNo) {
        Map<Long, Product> map = findProductsCache(categoryNo);

        List<ProductDto> list = new ArrayList<>();
        for (Long productNo : map.keySet()) {
            // 카테고리는 카테고리명이라는 단 1개의 속성만 갖는다.
            ProductDto productDto = new ProductDto();
            Product product = map.get(productNo);
            productDto.setProductName(product.getProductName());
            productDto.setProductPrice(product.getProductPrice());
            productDto.setCategoryNo(product.getCategoryNo());
            list.add(productDto);
        }
        return list;
    }

    public Map<Long, Product> findProductsCache(Integer categoryNo) {
        long now = System.currentTimeMillis();
        boolean dbQuery = true;

        // 이미 조회한 카테고리 상품리스트라면 캐시 값 리턴
        if (!categoryProductMap.isEmpty()) {
            if (categoryProductMap.get(categoryNo) != null) {
                dbQuery = false;
            }
        }

        if (dbQuery || now - productMapLoadTime > productMapCacheDuration) {
            synchronized (categoryProductMap) {
                if (dbQuery || now - productMapLoadTime > productMapCacheDuration) {

                    List<Product> products = productRepository.findByCategoryNo(categoryNo);
                    Map<Long, Product> productMap = new HashMap<>();

                    for (Product product : products) {
                        productMap.put(product.getProductNo(), product);
                    }
                    categoryProductMap.put(categoryNo, productMap);
                    productMapLoadTime = now;
                }
            }
        }
        return categoryProductMap.get(categoryNo);
    }

    // 상품 등록
    public Product addProduct(ProductAddForm productAddForm) {
        Product product = new Product();
        product.setProductName(productAddForm.getProductName());
        product.setBrandName(productAddForm.getBrandName());
        product.setProductPrice(productAddForm.getProductPrice());
        product.setCategoryNo(productAddForm.getCategoryNo());
        return productRepository.save(product);
    }

    // 상품 조회
    public ProductDto findProduct(Long productNo) {
        Product product = new Product();

        // 캐시에 없으면 DB 조회 후 캐시 저장
        if (lruCache.get(productNo) == null) {
            product = productRepository.findById(productNo).orElseThrow(() -> new IllegalArgumentException("해당 상품이 존재하지 않습니다. productNo=" + productNo));
            lruCache.put(productNo, product);
        } else {
            product = lruCache.get(productNo).getData();
        }
        lruCache.show();

        ProductDto productDto = new ProductDto();
        productDto.setProductName(product.getProductName());
        productDto.setProductPrice(product.getProductPrice());
        productDto.setCategoryNo(product.getCategoryNo());
        return productDto;
    }

    // 상품 수정
    public Product updateProduct(Long productNo, ProductModifyForm productForm) {
        Product product = productRepository.findById(productNo).orElseThrow(() -> new IllegalArgumentException("해당 상품이 존재하지 않습니다. productNo=" + productNo));

        if (productForm.getProductName() != null) {
            product.setProductName(productForm.getProductName());
        }
        if (productForm.getProductPrice() != null) {
            product.setProductPrice(productForm.getProductPrice());
        }
        return productRepository.save(product);
    }

    // 상품 삭제
    public void deleteProduct(Long productNo) {
        productRepository.findById(productNo).orElseThrow(() -> new IllegalArgumentException("해당 상품이 존재하지 않습니다. productNo=" + productNo));
        productRepository.deleteById(productNo);
    }
}
