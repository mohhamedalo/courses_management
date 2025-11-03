package com.example.demo2.controller;

import com.example.demo2.dto.request.exam.CreateExamRequest;
import com.example.demo2.dto.response.ApiResponse;
import com.example.demo2.dto.response.courses.CourseResponse;
import com.example.demo2.dto.response.exam.ExamResponse;
import com.example.demo2.service.ExamService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/exams")
public class ExamController {
    private final ExamService examService;

    @PostMapping
    public ResponseEntity<ApiResponse<List<ExamResponse>>> submitAnswers(
            @Valid @RequestBody List<CreateExamRequest> requests,
            HttpServletRequest servletRequest
    ){
        var responses = examService.saveAll(requests);

        return ResponseEntity.ok(ApiResponse.<List<ExamResponse>>builder()
                .success(true)
                .message("Get List Course successful")
                .data(responses)
                .meta(new ApiResponse.Meta(LocalDateTime.now(), servletRequest.getRequestURI()))
                .build());
    }
}
