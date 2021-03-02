package com.amorepacific.product.controller;

import com.amorepacific.product.controller.dto.ProductAddForm;
import com.amorepacific.product.controller.dto.ProductDto;
import com.amorepacific.product.controller.dto.ProductModifyForm;
import com.amorepacific.product.domain.Product;
import com.amorepacific.product.service.CacheService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
public class ProductController {

    private final CacheService cacheService;

    @Autowired
    public ProductController(CacheService cacheService) {
        this.cacheService = cacheService;
    }

    @ApiOperation(value = "특정 카테고리에 속한 상품 리스트", notes = "특정 카테고리에 속한 상품 리스트를 조회합니다.")
    @ResponseBody
    @GetMapping("/categories/{categoryNo}/products")
    @ApiImplicitParam(name = "categoryNo", value = "카테고리번호", example = "1")
    public ResponseEntity<?> productList(@PathVariable Integer categoryNo) {
        List<ProductDto> list= cacheService.findProducts(categoryNo);
        return new ResponseEntity(list, HttpStatus.OK);
    }

    @ApiOperation(value = "상품 조회", notes = "특정 상품을 조회합니다.")
    @ApiImplicitParam(name = "productNo", value = "상품번호", example = "1001")
    @ResponseBody
    @GetMapping("/products/{productNo}")
    public ResponseEntity<?> find(@PathVariable Long productNo) {
         return Optional.ofNullable(cacheService.findProduct(productNo))
                .map(products -> ResponseEntity.ok(products))
                .orElse(ResponseEntity.noContent().build());
    }

    @ApiOperation(value = "상품 등록", notes = "상품을 등록합니다.")
    @PostMapping("/products")
    public ResponseEntity<?> add(@RequestBody ProductAddForm productAddForm) {
        Product created = cacheService.addProduct(productAddForm);
        return new ResponseEntity(created, HttpStatus.CREATED);
    }

    @ApiOperation(value = "상품 수정", notes = "상품명 및 가격을 수정합니다.")
    @ApiImplicitParam(name = "productNo", value = "상품번호", example = "1001")
    @PutMapping("/products/{productNo}")
    public ResponseEntity<?> modify(@PathVariable Long productNo, @RequestBody ProductModifyForm productModifyForm) {
        Product updated = cacheService.updateProduct(productNo, productModifyForm);
        return new ResponseEntity(updated, HttpStatus.OK);
    }

    @ApiOperation(value = "상품 삭제", notes = "특정 상품을 삭제합니다.")
    @ApiImplicitParam(name = "productNo", value = "상품번호", example = "1001")
    @DeleteMapping("/products/{productNo}")
    public ResponseEntity<?> delete(@PathVariable Long productNo) {
        cacheService.deleteProduct(productNo);
        return new ResponseEntity(HttpStatus.OK);
    }

    @ApiOperation(value = "카테고리별 상품 리스트 캐시 클리어", notes = "카테고리별 상품 리스트 캐시 클리어.")
    @DeleteMapping("/products/cache/clear")
    public ResponseEntity<?> cacheClear() {
        cacheService.productCacheClear();
        return new ResponseEntity(HttpStatus.OK);
    }
}
