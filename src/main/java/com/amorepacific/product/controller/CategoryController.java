package com.amorepacific.product.controller;

import com.amorepacific.product.controller.dto.CategoryAddForm;
import com.amorepacific.product.controller.dto.CategoryDto;
import com.amorepacific.product.controller.dto.CategoryModifyForm;
import com.amorepacific.product.domain.Category;
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
public class CategoryController {

    private final CacheService cacheService;

    @Autowired
    public CategoryController(CacheService cacheService) {
        this.cacheService = cacheService;
    }

    @ApiOperation(value = "전체 카테고리 조회", notes = "전체 카테고리 리스트를 조회합니다.")
    @ResponseBody
    @GetMapping("/categories")
    public ResponseEntity<?> list() {
        List<CategoryDto> list = cacheService.findCategories();
        return new ResponseEntity(list, HttpStatus.OK);
    }

    @ApiOperation(value = "카테고리 조회", notes = "특정 카테고리를 조회합니다.")
    @ApiImplicitParam(name = "categoryNo", value = "카테고리번호", example = "11")
    @ResponseBody
    @GetMapping("/categories/{categoryNo}")
    public ResponseEntity<?> find(@PathVariable Integer categoryNo) {
        return Optional.ofNullable(cacheService.findCategory(categoryNo))
                .map(category -> ResponseEntity.ok(category))
                .orElse(ResponseEntity.noContent().build());
    }

    @ApiOperation(value = "카테고리 등록", notes = "카테고리를 등록합니다.")
    @PostMapping("/categories")
    public ResponseEntity<?> add(@RequestBody CategoryAddForm categoryAddForm) {
        Category created = cacheService.addCategory(categoryAddForm);
        return new ResponseEntity(created, HttpStatus.CREATED);
    }

    @ApiOperation(value = "카테고리 수정", notes = "특정 카테고리명을 수정합니다.")
    @ApiImplicitParam(name = "categoryNo", value = "카테고리번호", example = "11")
    @PutMapping("/categories/{categoryNo}")
    public ResponseEntity<?> modify(@PathVariable Integer categoryNo, @RequestBody CategoryModifyForm categoryModifyForm) {
        Category updated = cacheService.updateCategory(categoryNo, categoryModifyForm);
        return new ResponseEntity(updated, HttpStatus.OK);
    }

    @ApiOperation(value = "카테고리 삭제", notes = "특정 카테고리를 삭제합니다.")
    @ApiImplicitParam(name = "categoryNo", value = "카테고리번호", example = "11")
    @DeleteMapping("/categories/{categoryNo}")
    public ResponseEntity<?> delete(@PathVariable Integer categoryNo) {
        cacheService.deleteCategory(categoryNo);
        return new ResponseEntity(HttpStatus.OK);
    }

    @ApiOperation(value = "카테고리 캐시 클리어", notes = "카테고리 캐시 클리어.")
    @DeleteMapping("/categories/cache/clear")
    public ResponseEntity<?> cacheClear() {
        cacheService.categoryCacheClear();
        return new ResponseEntity(HttpStatus.OK);
    }
}
