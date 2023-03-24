package ru.practicum.category.mapper;

import lombok.experimental.UtilityClass;
import org.springframework.data.domain.Page;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.model.Category;

import java.util.Collection;
import java.util.stream.Collectors;

@UtilityClass
public class CategoryMapper {
    public static Category dtoToCategory(CategoryDto categoryDto) {
        return new Category(categoryDto.getId(), categoryDto.getName());
    }

    public static CategoryDto categoryToDto(Category category) {
        return new CategoryDto(category.getId(), category.getName());
    }

    public static Collection<CategoryDto> categoryToDtoCollection(Page<Category> categoryPage) {
        return categoryPage.stream().map(CategoryMapper::categoryToDto).collect(Collectors.toList());
    }
}
