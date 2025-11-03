package com.example.demo2.mapper;

import com.example.demo2.dto.response.exam.ExamResponse;
import com.example.demo2.entity.QuizAnswer;

public class ExamMapper {
    public static ExamResponse toResponse(QuizAnswer a) {
        return ExamResponse.builder()
                .id(a.getId())
                .answeredAt(a.getAnsweredAt())
                .isCorrect(a.getIsCorrect())
                .selectedAnswer(a.getSelectedAnswer())
                .quizId(a.getQuiz().getId())
                .studentId(a.getStudent().getId())
                .build();
    }
}
