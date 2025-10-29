package com.example.demo2.service;

import com.example.demo2.dto.request.quizzes.CreateQuizRequest;
import com.example.demo2.dto.response.quizzes.QuizResponse;
import com.example.demo2.entity.Lesson;
import com.example.demo2.entity.Quiz;
import com.example.demo2.mapper.QuizMapper;
import com.example.demo2.repository.QuizRepository;
import lombok.*;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Builder
@Service
public class QuizService {
    private final QuizRepository quizRepository;
    private final LessonService lessonService;

    public QuizResponse create(CreateQuizRequest request) {
        Lesson lesson = lessonService.findOne(request.getLessonId());

        Quiz newQuiz = Quiz.builder()
                .correctAnswer(request.getCorrectAnswer())
                .optionA(request.getOptionA())
                .optionB(request.getOptionB())
                .optionC(request.getOptionC())
                .optionD(request.getOptionD())
                .question(request.getQuestion())
                .lesson(lesson)
                .build();

        newQuiz = quizRepository.save(newQuiz);

        return QuizMapper.toResponse(newQuiz);
    }
}
