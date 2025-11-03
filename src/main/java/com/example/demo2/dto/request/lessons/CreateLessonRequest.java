package com.example.demo2.dto.request.lessons;

import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateLessonRequest {
    @NotBlank
    private String content;

//    @Null(message = "Order index can be null to append at the end")
    private Integer orderIndex;

    @NotBlank
    private String title;

    @NotBlank
    private String videoUrl;

    @NotNull(message = "Course ID is required")
    @Positive(message = "Course ID must be positive")
    private Long courseId;
}
