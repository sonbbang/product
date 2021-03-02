package com.amorepacific.product.controller.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
public class ProductDto {
    private String productName;
    private BigDecimal productPrice;
    private Integer categoryNo;
}
