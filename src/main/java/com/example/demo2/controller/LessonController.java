package com.example.demo2.controller;

import com.example.demo2.dto.request.lessons.CreateLessonRequest;
import com.example.demo2.dto.response.ApiResponse;
import com.example.demo2.dto.response.courses.CourseResponse;
import com.example.demo2.dto.response.lessons.LessonResponse;
import com.example.demo2.service.LessonService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@AllArgsConstructor
@RequestMapping("/api/lessons")
public class LessonController {
    private final LessonService lessonService;

    @PostMapping
    public ResponseEntity<ApiResponse<LessonResponse>> create(
            @Valid @RequestBody CreateLessonRequest request,
            HttpServletRequest servletRequest
    ) {
        var response = lessonService.create(request);

        return ResponseEntity.ok(ApiResponse.<LessonResponse>builder()
                .success(true)
                .message("Create Lesson successful")
                .data(response)
                .meta(new ApiResponse.Meta(LocalDateTime.now(), servletRequest.getRequestURI()))
                .build());
    }
}
