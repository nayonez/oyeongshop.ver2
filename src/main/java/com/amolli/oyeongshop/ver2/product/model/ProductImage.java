package com.amolli.oyeongshop.ver2.product.model;

import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Builder
@Setter
@Getter
@Entity
@Table(name = "tbl_product_image")
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor
public class ProductImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long prodDetailImgId;

    private String prodDetailImgName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="prod_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Product product;

}
