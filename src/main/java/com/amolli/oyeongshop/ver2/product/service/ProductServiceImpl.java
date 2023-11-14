package com.amolli.oyeongshop.ver2.product.service;

import com.amolli.oyeongshop.ver2.product.dto.ProductResponse;
import com.amolli.oyeongshop.ver2.product.model.Product;
import com.amolli.oyeongshop.ver2.product.model.ProductOption;
import com.amolli.oyeongshop.ver2.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
// Impl"은 "implementation"의 줄임말로 사용되며, 일반적으로 어떤 인터페이스(interface)나
// 추상 클래스(abstract class)를 구체적으로 구현한 클래스를 가리킬 때 사용
public class
ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;

    // ProductRepository에서 얻은 제품 목록을 반환
    @Override
    public List<ProductResponse> findProductAll() {
        List<Product> products = productRepository.findAll();
        List<ProductResponse> productList = new ArrayList<>();
        for (Product product : products) {
            productList.add(ProductResponse.from(product));
        }
        return productList;
    }

    // 상품 등록
    @Override
    public Product save(Product product) {
        System.out.println("상품이 등록되었습니다.");
        return productRepository.save(product);
    }

    // 상품 상세 정보 보기
    public Product findById(Long prodId){
        Optional<Product> OptionalProduct = productRepository.findById(prodId);

        if (OptionalProduct.isPresent()) {
            return OptionalProduct.get();
        }
        else {
            return null;
        }
    }

    // 상품을 카테고리별로 분류
    public List<ProductResponse> getProductsByCategory(String prodCategory) {
        List<Product> products = productRepository.findByProdCategoryJPQL(prodCategory);
        return products.stream()
            .map(product -> {
                ProductResponse dto = new ProductResponse();
                dto.setProdId(product.getProdId());
                dto.setProdName(product.getProdName());
                dto.setProdCategory(product.getProdCategory());
                dto.setProdSalesPrice(product.getProdSalesPrice());
                dto.setProdMainImgPath(product.getProdMainImgPath());
                // 다른 필드들 설정...
                return dto;
            })
            .collect(Collectors.toList());
    }

    // 상품 옵션 중복 제거
    public Product removeDuplicateOptions(Product product) {
        List<ProductOption> productOptions = product.getProductOptions();
        Set<String> uniqueColors = new HashSet<>();
        Set<String> uniqueSizes = new HashSet<>();
        List<ProductOption> uniqueProductOptions = new ArrayList<>();

        for (ProductOption option : productOptions) {
            // 색상 중복 확인
            if (uniqueColors.add(option.getProdOptColor())) {
                uniqueProductOptions.add(option);
            }
        }
        product.setProductOptions(uniqueProductOptions);
        return product;
    }

    // 전체 상품 수 조회
    public int getTotalProductCount() {
        return productRepository.findAll().size();
    }

    // 페이징된 상품 목록 조회
    public List<ProductResponse> findProductPaged(int page, int itemsPerPage) {
        PageRequest pageRequest = PageRequest.of(page - 1, itemsPerPage);
        Page<Product> productPage = productRepository.findAll(pageRequest);

        return productPage.getContent().stream()
                .map(product -> {
                    ProductResponse dto = new ProductResponse();
                    dto.setProdId(product.getProdId());
                    dto.setProdName(product.getProdName());
                    dto.setProdCode(product.getProdCode());
                    dto.setProdCategory(product.getProdCategory());
                    dto.setProdRegDate(product.getProdRegDate());
                    // 다른 필드들 설정...
                    return dto;
                })
                .collect(Collectors.toList());
    }
}
