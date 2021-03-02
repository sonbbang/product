package com.amorepacific.product.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "label_sequence")
    @SequenceGenerator(name = "label_sequence", sequenceName = "CATEGORY_SEQ", allocationSize = 1)
    @Column(name="CATEGORY_NO")
    private Integer CategoryNo;
    @Column(name="CATEGORY_NAME")
    private String CategoryName;
    @Column(name="PARENT_NO")
    private Integer ParentNo;
    @Column(name="DEPTH")
    private Integer depth;

}
