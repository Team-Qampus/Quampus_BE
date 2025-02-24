package swyp.qampus.category.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "Category")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "category_id")
    private long categoryId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, unique = true, length = 50)
    private CategoryType categoryName;

    public static Category of(CategoryType categoryName) {
        return Category.builder()
                .categoryName(categoryName)
                .build();
    }
}
