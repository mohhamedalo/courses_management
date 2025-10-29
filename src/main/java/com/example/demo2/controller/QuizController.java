package com.example.demo2.controller;

import com.example.demo2.dto.request.quizzes.CreateQuizRequest;
import com.example.demo2.dto.response.ApiResponse;
import com.example.demo2.dto.response.lessons.LessonResponse;
import com.example.demo2.dto.response.quizzes.QuizResponse;
import com.example.demo2.service.QuizService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@AllArgsConstructor
@RequestMapping("/api/quizzes")
public class QuizController {
    private final QuizService quizService;

    @PostMapping
    public ResponseEntity<ApiResponse<?>> create(
            @Valid @RequestBody CreateQuizRequest request,
            HttpServletRequest servletRequest
    ) {
        var response = quizService.create(request);

        return ResponseEntity.ok(ApiResponse.<QuizResponse>builder()
                .success(true)
                .message("Create Quiz successful")
                .data(response)
                .meta(new ApiResponse.Meta(LocalDateTime.now(), servletRequest.getRequestURI()))
                .build());
    }

}
