package com.example.demo2.dto.request.quizzes;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.util.List;

@Getter @Setter @Builder
@AllArgsConstructor
@NoArgsConstructor
public class BulkCreateRequest {
//    @JsonProperty("course_id")
//    @NotNull(message = "Course ID is required")
//    @Positive(message = "Course ID must be positive")
//    private Integer courseId;

    @JsonProperty("lesson_id")
    @NotNull(message = "Lesson ID is required")
    @Positive(message = "Lesson ID must be positive")
    private Integer lessonId;

    @JsonProperty("quizzes")
    private List<CreateQuizWithoutLessonFieldRequest> quizzes;
}
