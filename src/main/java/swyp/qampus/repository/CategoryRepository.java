package swyp.qampus.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import swyp.qampus.entity.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
