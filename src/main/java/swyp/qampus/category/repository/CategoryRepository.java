package swyp.qampus.category.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import swyp.qampus.category.domain.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
