package com.example.demo2.dto.request.quizzes;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter @Setter @Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateQuizWithoutLessonFieldRequest {
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
}
