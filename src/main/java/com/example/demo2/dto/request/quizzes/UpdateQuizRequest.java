package com.example.demo2.dto.request.quizzes;

import jakarta.validation.constraints.Pattern;
import lombok.*;

@Getter @Setter @Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateQuizRequest {
    private String question;
    private String optionA;
    private String optionB;
    private String optionC;
    private String optionD;

    @Pattern(regexp = "^[A-D]$", message = "correctAnswer must be A, B, C or D")
    private String correctAnswer;
}
