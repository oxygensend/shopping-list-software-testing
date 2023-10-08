package com.oxygensend.backend.infrastructure.shopping_list.repository;

import com.oxygensend.backend.domain.shooping_list.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID>, com.oxygensend.backend.domain.shooping_list.ProductRepository {
}
