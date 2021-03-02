package com.amorepacific.product.controller.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CategoryAddForm {
    @ApiModelProperty(example = "카테고리명추가")
    private String categoryName;
    @ApiModelProperty
    private Integer ParentNo;
    @ApiModelProperty(example = "1")
    private Integer depth;

}
