package com.amorepacific.product.controller.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
public class ProductModifyForm {
    @ApiModelProperty(example = "상품명수정")
    private String productName;
    @ApiModelProperty(example = "11.11")
    private BigDecimal productPrice;
}
