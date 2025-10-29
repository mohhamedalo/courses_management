package com.example.demo2.dto.response.exam;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class ExamResponse {
    private Long id;
    private Long quizId;
    private Long studentId;
    private String selectedAnswer;
    private Boolean isCorrect;
    private LocalDateTime answeredAt;
}
