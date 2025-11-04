package com.example.demo2.service;

import com.example.demo2.dto.request.quizzes.BulkCreateRequest;
import com.example.demo2.dto.request.quizzes.BulkDeleteRequest;
import com.example.demo2.dto.request.quizzes.CreateQuizRequest;
import com.example.demo2.dto.request.quizzes.UpdateQuizRequest;
import com.example.demo2.dto.response.quizzes.QuizResponse;
import com.example.demo2.entity.Lesson;
import com.example.demo2.entity.Quiz;
import com.example.demo2.exception.ResourceNotFoundException;
import com.example.demo2.mapper.QuizMapper;
import com.example.demo2.repository.LessonRepository;
import com.example.demo2.repository.QuizRepository;
import jakarta.transaction.Transactional;
import lombok.*;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Builder
@Service
public class QuizService {
    private final QuizRepository quizRepository;
    private final LessonRepository lessonRepository;
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

    public List<Quiz> bulkCreate(BulkCreateRequest request) {

        List<Quiz> quizzes = request.getQuizzes()
                .stream()
                .map(quiz -> {
                    Lesson l = lessonService.findOne(Long.valueOf(request.getLessonId()));

                    return Quiz.builder()
                            .lesson(l)
                            .question(quiz.getQuestion())
                            .optionA(quiz.getOptionA())
                            .optionB(quiz.getOptionB())
                            .optionC(quiz.getOptionC())
                            .optionD(quiz.getOptionD())
                            .correctAnswer(quiz.getCorrectAnswer())
                            .build();
                })
                .toList();

        return quizRepository.saveAll(quizzes);
    }

    @Transactional
    public QuizResponse update(Long quizId, UpdateQuizRequest request) {
        Quiz q = quizRepository.findById(quizId)
                .orElseThrow(() -> new ResourceNotFoundException("quiz not found: " + quizId));

        if (request.getQuestion() != null && !request.getQuestion().isBlank()) {
            q.setQuestion(request.getQuestion());
        }

        if (request.getOptionA() != null && !request.getOptionA().isBlank()) {
            q.setOptionA(request.getOptionA());
        }

        if (request.getOptionB() != null && !request.getOptionB().isBlank()) {
            q.setOptionB(request.getOptionB());
        }

        if (request.getOptionC() != null && !request.getOptionC().isBlank()) {
            q.setOptionC(request.getOptionC());
        }

        if (request.getOptionD() != null && !request.getOptionD().isBlank()) {
            q.setOptionD(request.getOptionD());
        }

        q = quizRepository.save(q);

        return QuizMapper.toResponse(q);

    }

    @Transactional
    public void delete(Long quizId) {
        Quiz q = quizRepository.findById(quizId)
                .orElseThrow(() -> new ResourceNotFoundException("quiz not found: " + quizId));

        quizRepository.delete(q);
    }

    @Transactional
    public void bulkDelete(BulkDeleteRequest request) {
        List<Quiz> quizzes = quizRepository.findAllById(request.getIds());

        if (!quizzes.isEmpty()) {
            quizRepository.deleteAll(quizzes);
        }
    }
}
