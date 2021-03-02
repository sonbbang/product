package com.amorepacific.product.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Getter
@Setter
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "label_sequence")
    @SequenceGenerator(name = "label_sequence", sequenceName = "PRODUCT_SEQ", allocationSize = 1)
    @Column(name="PRODUCT_NO")
    private Long productNo;
    @Column(name="BRAND_NAME")
    private String brandName;
    @Column(name="PRODUCT_NAME")
    private String productName;
    @Column(name="PRODUCT_PRICE", precision = 8, scale = 2)
    private BigDecimal productPrice;
    @Column(name="CATEGORY_NO")
    private Integer categoryNo;
}
