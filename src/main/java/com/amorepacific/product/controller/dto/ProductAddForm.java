package com.amorepacific.product.controller.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
public class ProductAddForm {
    @ApiModelProperty(example = "브랜드명추가")
    private String brandName;
    @ApiModelProperty(example = "상품명추가")
    private String productName;
    @ApiModelProperty(example = "11.11")
    private BigDecimal productPrice;
    @ApiModelProperty(example = "1")
    private Integer categoryNo;
}
