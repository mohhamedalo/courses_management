package com.example.demo2.controller;

import com.example.demo2.dto.request.enrollments.FilterEnrollmentRequest;
import com.example.demo2.dto.request.exam.CreateExamRequest;
import com.example.demo2.dto.response.ApiResponse;
import com.example.demo2.dto.response.courses.CourseResponse;
import com.example.demo2.dto.response.enrollments.EnrollmentResponse;
import com.example.demo2.dto.response.exam.ExamResponse;
import com.example.demo2.service.ExamService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/exams")
@Tag(name = "Exam API", description = "Operations related to exams")
public class ExamController {
    private final ExamService examService;

    @Operation(
            summary = "Create enrollment ",
            description = "Returns a category wrapped in ApiResponse"
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Successfully create enrollment",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponse.class)
                    )
            )
    })
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
