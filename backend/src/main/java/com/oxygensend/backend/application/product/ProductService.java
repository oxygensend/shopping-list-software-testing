package com.oxygensend.backend.application.product;

import com.oxygensend.backend.application.product.dto.ProductsResponse;
import com.oxygensend.backend.domain.shooping_list.Grammar;
import com.oxygensend.backend.domain.shooping_list.Product;
import com.oxygensend.backend.infrastructure.shopping_list.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductsResponse getProducts() {
        var products = productRepository.findAll();
        var grammarNames = Arrays.stream(Grammar.values()).collect(Collectors.toSet());
        return new ProductsResponse(products.stream().map(Product::name).collect(Collectors.toSet()), grammarNames);
    }

}
