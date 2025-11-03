package com.example.demo2.mapper;

import com.example.demo2.dto.response.quizzes.QuizResponse;
import com.example.demo2.entity.Quiz;

public class QuizMapper {
    public static QuizResponse toResponse(Quiz q) {
        return QuizResponse.builder()
                .id(q.getId())
                .correctAnswer(q.getCorrectAnswer())
                .optionA(q.getOptionA())
                .optionB(q.getOptionB())
                .optionC(q.getOptionC())
                .optionD(q.getOptionD())
                .question(q.getQuestion())
                .lessonId(q.getLesson().getId())
                .build();
    }
}
