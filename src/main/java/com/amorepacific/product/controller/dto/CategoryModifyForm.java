package com.amorepacific.product.controller.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CategoryModifyForm {
    @ApiModelProperty(example = "카테고리명수정")
    private String categoryName;

}
