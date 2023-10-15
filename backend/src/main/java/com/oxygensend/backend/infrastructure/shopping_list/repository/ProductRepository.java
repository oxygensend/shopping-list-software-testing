package com.oxygensend.backend.infrastructure.shopping_list.repository;

import com.oxygensend.backend.domain.shooping_list.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID>, com.oxygensend.backend.domain.shooping_list.ProductRepository {


    @Override
    @Query("select p from Product p where p.name in :names")
    List<Product> findByNames(@Param("names") Set<String> names);
}
