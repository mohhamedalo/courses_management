package com.example.demo2.dto.request.course;

import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateCourseRequest {
    @NotBlank
    private String description;

    @NotBlank
    private String level;

    @NotNull(message = "Price is required")
    @PositiveOrZero(message = "Price must be zero or positive")
    @DecimalMax(value = "1000000.00", message = "Price must not exceed 1,000,000")
    private BigDecimal price;

    @NotBlank
    private String status;

    @NotBlank
    private String thumbnailUrl;

    @NotBlank
    private String title;

    @NotNull(message = "Category ID is required")
    @Positive(message = "Category ID must be positive")
    private Integer categoryId;

//    @NotNull(message = "Category ID is required")
//    @Positive(message = "Category ID must be positive")
//    private Integer instructorId;


}
