package com.example.demo2.dto.request.course;

import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateCourseRequest {
    @Size(min = 1, max = 200, message = "Title must be 1-200 characters")
    private String title;

    @Size(max = 2000, message = "Description must not exceed 2000 characters")
    private String description;

    @Size(min = 1, max = 50, message = "Level must be 1-50 characters")
    private String level;

    @PositiveOrZero(message = "Price must be zero or positive")
    @DecimalMax(value = "1000000.00", message = "Price must not exceed 1,000,000")
    private BigDecimal price;

    @Size(min = 1, max = 20, message = "Status must be 1-20 characters")
    private String status;

    @Size(max = 500, message = "Thumbnail URL must not exceed 500 characters")
    private String thumbnailUrl;

    @Positive(message = "Category ID must be positive")
    private Integer categoryId;
}
