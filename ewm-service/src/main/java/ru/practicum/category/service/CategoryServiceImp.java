package ru.practicum.category.service;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.model.Category;
import ru.practicum.category.repository.CategoryRepository;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.exception.ConflictException;
import ru.practicum.exception.NotFoundException;

import java.util.Collection;

import static ru.practicum.category.mapper.CategoryMapper.*;

@Service
public class CategoryServiceImp implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final EventRepository eventRepository;

    public CategoryServiceImp(CategoryRepository categories, EventRepository eventRepository) {
        this.categoryRepository = categories;
        this.eventRepository = eventRepository;
    }

    @Transactional
    @Override
    public void createCategory(CategoryDto categoryDto) {
        categoryRepository.save(dtoToCategory(categoryDto));
    }

    @Transactional
    @Override
    public CategoryDto updateCategory(Long categoryId, CategoryDto updateCategory) {
        categoryRepository.findById(categoryId)
                .orElseThrow(() -> new NotFoundException("Категория с id: " + categoryId + " не найдена."));
        return categoryToDto(categoryRepository.save(dtoToCategory(updateCategory)));
    }

    @Transactional
    @Override
    public void deleteCategory(Long categoryId) {
        categoryRepository.findById(categoryId)
                .orElseThrow(() -> new NotFoundException("Категория с id: " + categoryId + " не найдена."));
        if (!eventRepository.findAllByCategoryId(categoryId).isEmpty()) {
            throw new ConflictException("Категория не пустая.");
        }
        categoryRepository.deleteById(categoryId);
    }

    @Transactional(readOnly = true)
    @Override
    public Collection<CategoryDto> getCategories(Pageable pageable) {
        return categoryToDtoCollection(categoryRepository.findAll(pageable));
    }

    @Transactional(readOnly = true)
    @Override
    public CategoryDto getCategoryById(Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new NotFoundException("Категория с id: " + categoryId + " не найдена."));
        return categoryToDto(category);
    }
}
