package ru.practicum.category.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.category.model.Category;

@Repository
public interface CategoryRepository extends PagingAndSortingRepository<Category, Long> {
    boolean existsByName(String name);
}
