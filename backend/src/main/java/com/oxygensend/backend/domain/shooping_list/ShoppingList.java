package com.oxygensend.backend.domain.shooping_list;

import com.oxygensend.backend.domain.auth.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.*;
import lombok.experimental.Accessors;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;


@Entity
@Getter
@Setter
@Accessors(chain = true, fluent = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ShoppingList {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(length = 64)
    private String name;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "shoppingList", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<ListElement> listElements = new HashSet<>();

    @Column(nullable = false)
    private boolean completed = false;

    @Column(nullable = true)
    private String imageAttachmentFilename;

    @Column
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    private LocalDateTime dateOfExecution;

    public void addListElement(ListElement element) {
        if (listElements.contains(element)) {
            return;
        }

        listElements.add(element);
        element.shoppingList(this);
    }
}
