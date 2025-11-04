package com.example.demo2.dto.request.quizzes;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

@Setter @Getter @Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateQuizRequest {
    @NotBlank
    private String correctAnswer;

    @NotBlank
    private String optionA;

    @NotBlank
    private String optionB;

    @NotBlank
    private String optionC;

    @NotBlank
    private String optionD;

    @NotBlank
    private String question;

    @NotNull(message = "Lesson ID is required")
    @Positive(message = "Lesson ID must be positive")
    private Long lessonId;
}
