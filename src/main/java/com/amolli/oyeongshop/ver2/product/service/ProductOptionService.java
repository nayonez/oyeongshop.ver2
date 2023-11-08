package com.amolli.oyeongshop.ver2.product.service;

import com.amolli.oyeongshop.ver2.product.model.ProductOption;

import java.util.List;

// 코드 재사용성과 확장성, 프레임워크 제작등을 위해 추상 클래스나 인터페이스를 사용한다.
public interface ProductOptionService {
    List<ProductOption> findProductOptionAll();
}