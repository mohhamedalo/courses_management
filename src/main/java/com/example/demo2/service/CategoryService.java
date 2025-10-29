package com.example.demo2.service;

import com.example.demo2.dto.request.CreateCategoryRequest;
import com.example.demo2.dto.request.category.UpdateCategoryRequest;
import com.example.demo2.dto.response.CategoryResponse;
import com.example.demo2.entity.Category;
import com.example.demo2.entity.User;
import com.example.demo2.exception.ResourceNotFoundException;
import com.example.demo2.mapper.CategoryMapper;
import com.example.demo2.repository.CategoryRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public CategoryResponse create(CreateCategoryRequest request) {

        Category c = Category.builder()
                .name(request.getName())
                .description(request.getDescription())
                .build();

        c = categoryRepository.save(c);

        return new CategoryResponse(c.getId(), c.getName(), c.getDescription());
    }

    public Page<CategoryResponse> getAll(Pageable pageable) {
        return categoryRepository.findAll(pageable).map(CategoryMapper::toResponse);
    }

    public Optional<Category> findOne(Long categoryId) {
        return categoryRepository.findById(categoryId);
    }

    @Transactional
    public CategoryResponse update(Long categoryId, UpdateCategoryRequest request) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + categoryId));

        // Only update fields that are NOT null and NOT blank
        if (request.getName() != null && !request.getName().isBlank()) {
            category.setName(request.getName().trim());
        }
        if (request.getDescription() != null && !request.getDescription().isBlank()) {
            category.setDescription(request.getDescription().trim());
        }

        Category updated = categoryRepository.save(category);

//        return new CategoryResponse(updated.getId(), updated.getName(), updated.getDescription());
        return CategoryMapper.toResponse(updated);
    }

    public String delete(Long categoryId) {

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + categoryId));

        categoryRepository.delete(category);

        return "Delete successfully";

    }

    public Category findOneById(Long categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + categoryId));
    }
}
