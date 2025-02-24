package swyp.qampus.category.repository;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import swyp.qampus.category.domain.Category;
import swyp.qampus.category.domain.CategoryType;

import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class CategoryInitializer {
    private final CategoryRepository categoryRepository;

    @PostConstruct
    public void initCategories() {
        // DB에 기존 카테고리가 없을 경우, 기본 카테고리 삽입
        if (categoryRepository.count() == 0) {
            List<Category> categories = Arrays.stream(CategoryType.values())
                    .map(Category::of)
                    .toList();

            categoryRepository.saveAll(categories);
        }
    }
}
