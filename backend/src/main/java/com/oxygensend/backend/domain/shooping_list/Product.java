package com.oxygensend.backend.domain.shooping_list;

import com.oxygensend.backend.application.shopping_list.dto.ProductDto;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.Accessors;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true, fluent = true)
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(length = 64, nullable = false)
    private String name;

    @Column
    @CreationTimestamp
    private LocalDateTime createdAt;

    public Product(String name) {
        this.name = name;
    }


    public static Product productDto(ProductDto dto) {
        return new Product(dto.name());
    }

}
