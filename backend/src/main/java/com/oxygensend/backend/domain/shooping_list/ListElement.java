package com.oxygensend.backend.domain.shooping_list;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.Accessors;

import java.util.UUID;

@Entity
@Getter
@Setter
@Accessors(chain = true, fluent = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ListElement {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "shopping_list_id", nullable = false)
    private ShoppingList shoppingList;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Grammar grammar = Grammar.PIECE;

    @Column(nullable = false)
    private boolean completed = false;

    @Column(nullable = false)
    private float quantity = 0;
}
