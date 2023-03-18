package ru.practicum.category.service;

import org.springframework.data.domain.Pageable;
import ru.practicum.category.dto.CategoryDto;

import java.util.Collection;

public interface CategoryService {
    void createCategory(CategoryDto categoryDto);

    CategoryDto updateCategory(Long categoryId, CategoryDto categoryDto);

    void deleteCategory(Long categoryId);

    Collection<CategoryDto> getCategories(Pageable pageable);

    CategoryDto getCategoryById(Long categoryId);
}
