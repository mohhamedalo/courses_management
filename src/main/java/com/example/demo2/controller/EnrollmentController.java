package com.example.demo2.controller;

import com.example.demo2.dto.request.enrollments.EnrollmentRequest;
import com.example.demo2.dto.response.ApiResponse;
import com.example.demo2.dto.response.courses.CourseResponse;
import com.example.demo2.dto.response.enrollments.EnrollmentResponse;
import com.example.demo2.entity.Enrollment;
import com.example.demo2.service.EnrollmentService;
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
@RequestMapping("/api/enrollments")
public class EnrollmentController {
    private final EnrollmentService enrollmentService;

    @PostMapping
    public ResponseEntity<ApiResponse<?>> create(
            @Valid @RequestBody EnrollmentRequest request,
            HttpServletRequest servletRequest
    ) {
        var response = enrollmentService.create(request);

        return ResponseEntity.ok(ApiResponse.<EnrollmentResponse>builder()
                .success(true)
                .message("Create enrollment successful")
                .data(response)
                .meta(new ApiResponse.Meta(LocalDateTime.now(), servletRequest.getRequestURI()))
                .build());



    }

}
