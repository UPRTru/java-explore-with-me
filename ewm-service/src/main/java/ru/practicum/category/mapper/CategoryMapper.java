package ru.practicum.category.mapper;

import org.springframework.data.domain.Page;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.model.Category;

import java.util.Collection;
import java.util.stream.Collectors;

public class CategoryMapper {
    public static Category dtoToCategory(CategoryDto categoryDto) {
        return Category.builder()
                .id(categoryDto.getId())
                .name(categoryDto.getName())
                .build();
    }

    public static CategoryDto categoryToDto(Category category) {
        return CategoryDto.builder()
                .id(category.getId())
                .name(category.getName())
                .build();
    }

    public static Collection<CategoryDto> categoryToDtoCollection(Page<Category> categoryPage) {
        return categoryPage.stream().map(CategoryMapper::categoryToDto).collect(Collectors.toList());
    }
}
