package com.example.demo2.controller;

import com.example.demo2.dto.request.CreateCategoryRequest;
import com.example.demo2.dto.request.LoginRequest;
import com.example.demo2.dto.request.RegisterRequest;
import com.example.demo2.dto.request.category.UpdateCategoryRequest;
import com.example.demo2.dto.response.ApiResponse;
import com.example.demo2.dto.response.AuthResponse;
import com.example.demo2.dto.response.CategoryResponse;
import com.example.demo2.entity.Category;
import com.example.demo2.service.CategoryService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/categories")
@AllArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    @PostMapping
    public ResponseEntity<ApiResponse<CategoryResponse>> create(@Valid @RequestBody CreateCategoryRequest request, HttpServletRequest servletRequest) {
        CategoryResponse response = categoryService.create(request);

        return ResponseEntity.ok(ApiResponse.<CategoryResponse>builder()
                .success(true)
                .message("Create Category successful")
                .data(response)
                .meta(new ApiResponse.Meta(LocalDateTime.now(), servletRequest.getRequestURI()))
                .build());
    }

//    @GetMapping
//    public Page<CategoryResponse> getAll(Pageable pageable) {
//        return categoryService.getAll(pageable);
//    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<CategoryResponse>>> getAll(Pageable pageable, HttpServletRequest servletRequest) {

        var categories = categoryService.getAll(pageable);

        return ResponseEntity.ok(ApiResponse.<Page<CategoryResponse>>builder()
            .success(true)
            .message("Get List Category successful")
            .data(categories)
            .meta(new ApiResponse.Meta(LocalDateTime.now(), servletRequest.getRequestURI()))
            .build());
    }

    @PutMapping("/{categoryId}/update")
    public ResponseEntity<ApiResponse<CategoryResponse>> update(
            @PathVariable @Positive Long categoryId,
            @Valid @RequestBody UpdateCategoryRequest request,
            HttpServletRequest servletRequest
    ) {
        var newCategory = categoryService.update(categoryId, request);

        return ResponseEntity.ok(ApiResponse.<CategoryResponse>builder()
                .success(true)
                .message("Update Category successful")
                .data(newCategory)
                .meta(new ApiResponse.Meta(LocalDateTime.now(), servletRequest.getRequestURI()))
                .build());
    }

    @DeleteMapping("/{categoryId}/delete")
    public ResponseEntity<ApiResponse<?>> delete(
        @PathVariable @Positive Long categoryId,
        HttpServletRequest servletRequest
    ) {
        String response = categoryService.delete(categoryId);

        return ResponseEntity.ok(ApiResponse.<CategoryResponse>builder()
                .success(true)
                .message(response)
                .data(null)
                .meta(new ApiResponse.Meta(LocalDateTime.now(), servletRequest.getRequestURI()))
                .build());
    }
}
