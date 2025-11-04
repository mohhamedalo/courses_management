package com.example.demo2.controller;

import com.example.demo2.dto.request.CreateCategoryRequest;
import com.example.demo2.dto.request.category.UpdateCategoryRequest;
import com.example.demo2.dto.response.ApiResponse;
import com.example.demo2.dto.response.CategoryResponse;
import com.example.demo2.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/categories")
@Tag(name = "Category API", description = "Operations related to categories")
@AllArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    @Operation(
            summary = "Create categories ",
            description = "Returns a category wrapped in ApiResponse"
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Successfully create category",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponse.class)
                    )
            )
    })
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


    @Operation(
            summary = "Get all categories with pagination",
            description = "Returns a paginated list of categories wrapped in ApiResponse"
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved list",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponse.class)
                    )
            )
    })
    @GetMapping
    public ResponseEntity<ApiResponse<Page<CategoryResponse>>> getAll(
            @Parameter(description = "Pagination and sorting info (e.g., page=0&size=10&sort=name,asc&sort=id,desc)")
            Pageable pageable,
            HttpServletRequest servletRequest) {

        var categories = categoryService.getAll(pageable);

        return ResponseEntity.ok(ApiResponse.<Page<CategoryResponse>>builder()
            .success(true)
            .message("Get List Category successful")
            .data(categories)
            .meta(new ApiResponse.Meta(LocalDateTime.now(), servletRequest.getRequestURI()))
            .build());
    }

    @Operation(
            summary = "Get detail category",
            description = "Returns a category wrapped in ApiResponse"
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Successfully get detail category",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponse.class)
                    )
            )
    })
    @GetMapping("/{categoryId}/detail")
    public ResponseEntity<ApiResponse<CategoryResponse>> detail(
            @PathVariable @Positive Long categoryId,
            HttpServletRequest servletRequest
    ) {
        var category = categoryService.getOneCategory(categoryId);

        return ResponseEntity.ok(ApiResponse.<CategoryResponse>builder()
                .success(true)
                .message("Display Category successful")
                .data(category)
                .meta(new ApiResponse.Meta(LocalDateTime.now(), servletRequest.getRequestURI()))
                .build());
    }

    @Operation(
            summary = "Update category",
            description = "Returns a category wrapped in ApiResponse"
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Successfully update category",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponse.class)
                    )
            )
    })
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

    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Successfully delete category",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponse.class)
                    )
            )
    })
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
