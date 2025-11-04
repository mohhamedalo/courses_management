package com.example.demo2.service;

import com.example.demo2.dto.request.exam.CreateExamRequest;
import com.example.demo2.dto.response.exam.ExamResponse;
import com.example.demo2.entity.Quiz;
import com.example.demo2.entity.QuizAnswer;
import com.example.demo2.entity.User;
import com.example.demo2.exception.ResourceNotFoundException;
import com.example.demo2.mapper.ExamMapper;
import com.example.demo2.repository.QuizAnswerRepository;
import com.example.demo2.repository.QuizRepository;
import com.example.demo2.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
@Builder
public class ExamService {
    private final QuizAnswerRepository quizAnswerRepository;
    private final UserRepository userRepository;
    private final QuizRepository quizRepository;

    public List<ExamResponse> saveAll(List<CreateExamRequest> requests) {
        if (requests.isEmpty()) {
            throw new IllegalArgumentException("No answers provided");
        }

        // Optional: Validate all studentId are the same
        Long studentId = requests.get(0).getStudentId();
        if (requests.stream().anyMatch(r -> !r.getStudentId().equals(studentId))) {
            throw new IllegalArgumentException("All answers must belong to the same student");
        }

        // Load shared entities once
        User student = userRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found: " + studentId));

        // Build entities
        List<QuizAnswer> answers = requests.stream()
                .map(req -> {
                    Quiz quiz = quizRepository.findById(req.getQuizId())
                            .orElseThrow(() -> new ResourceNotFoundException("Quiz not found: " + req.getQuizId()));

                    return QuizAnswer.builder()
                            .quiz(quiz)
                            .student(student)
                            .selectedAnswer(req.getSelectedAnswer())
                            .isCorrect(determineCorrectness(quiz, req.getSelectedAnswer())) // optional logic
                            .build();
                })
                .toList();

        // Batch insert
        List<QuizAnswer> saved = quizAnswerRepository.saveAll(answers);

        return saved.stream()
                .map(ExamMapper::toResponse)
                .toList();
    }

    private Boolean determineCorrectness(Quiz quiz, String selectedAnswer) {
        // Your logic: compare with correct answer
        return quiz.getCorrectAnswer().equals(selectedAnswer);
    }
}
