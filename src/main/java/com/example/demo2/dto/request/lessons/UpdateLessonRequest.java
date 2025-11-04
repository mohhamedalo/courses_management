package com.example.demo2.dto.request.lessons;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateLessonRequest {
//    @NotBlank
    private String content;

    //    @Null(message = "Order index can be null to append at the end")
    private Integer orderIndex;

//    @NotBlank
    private String title;

//    @NotBlank
    private String videoUrl;

//    @NotNull(message = "Course ID is required")
    @Positive(message = "Course ID must be positive")
    private Long courseId;
}