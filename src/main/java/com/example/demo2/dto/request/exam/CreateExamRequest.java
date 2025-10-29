package com.example.demo2.dto.request.exam;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder

public class CreateExamRequest {
    @NotNull(message = "Quiz ID is required")
    private Long quizId;

    @NotBlank(message = "Selected answer is required")
    private String selectedAnswer;

    @NotNull(message = "Student ID is required")
    private Long studentId;
}
