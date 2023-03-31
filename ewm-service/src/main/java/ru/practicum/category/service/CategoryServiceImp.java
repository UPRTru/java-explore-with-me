package ru.practicum.category.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryServiceImp implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final EventRepository eventRepository;

    @Transactional
    @Override
    public CategoryDto createCategory(CategoryDto categoryDto) {
        if (categoryRepository.existsByName(categoryDto.getName())) {
            log.info("Имя категории уже занято: {}", categoryDto.getName());
            throw new ConflictException("Имя категории уже занято: " + categoryDto.getName());
        }
        Category category = dtoToCategory(categoryDto);
        log.info("Добавлена новая категория в базу данных: {}", category);
        return categoryToDto(categoryRepository.save(category));
    }

    @Transactional
    @Override
    public CategoryDto updateCategory(Long categoryId, CategoryDto updateCategory) {
        if (!categoryRepository.existsById(categoryId)) {
            log.info("Категория с id: {} не найдена.", categoryId);
            throw new NotFoundException("Категория с id: " + categoryId + " не найдена.");
        }
        if (categoryRepository.existsByName(updateCategory.getName())) {
            log.info("Имя категории уже занято: {}", updateCategory.getName());
            throw new ConflictException("Имя категории уже занято: " + updateCategory.getName());
        }
        Category category = dtoToCategory(updateCategory);
        log.info("Категория обновлена. {}", category);
        return categoryToDto(categoryRepository.save(category));
    }

    @Transactional
    @Override
    public void deleteCategory(Long categoryId) {
        if (!categoryRepository.existsById(categoryId)) {
            log.info("Категория с id: {} не найдена.", categoryId);
            throw new NotFoundException("Категория с id: " + categoryId + " не найдена.");
        }
        if (!eventRepository.findAllByCategoryId(categoryId).isEmpty()) {
            log.info("Категория di: {} не пустая.", categoryId);
            throw new ConflictException("Категория di: " + categoryId + " не пустая.");
        }
        log.info("Категория с id: {} удалена.", categoryId);
        categoryRepository.deleteById(categoryId);
    }

    @Transactional(readOnly = true)
    @Override
    public Collection<CategoryDto> getCategories(Pageable pageable) {
        log.info("Получение списка категорий.");
        return categoryToDtoCollection(categoryRepository.findAll(pageable));
    }

    @Transactional(readOnly = true)
    @Override
    public CategoryDto getCategoryById(Long categoryId) {
        Category category;
        try {
            category = categoryRepository.findById(categoryId)
                    .orElseThrow(() -> new NotFoundException("Категория с id: " + categoryId + " не найдена."));
        } catch (NotFoundException e) {
            log.info("Категория с id: {} не найдена.", categoryId);
            throw new NotFoundException(e.getMessage());
        }
        log.info("Получение категории id: {}", categoryId);
        return categoryToDto(category);
    }
}
