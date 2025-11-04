package com.example.demo2.controller;

import com.example.demo2.dto.request.quizzes.BulkCreateRequest;
import com.example.demo2.dto.request.quizzes.BulkDeleteRequest;
import com.example.demo2.dto.request.quizzes.CreateQuizRequest;
import com.example.demo2.dto.request.quizzes.UpdateQuizRequest;
import com.example.demo2.dto.response.ApiResponse;
import com.example.demo2.dto.response.lessons.LessonResponse;
import com.example.demo2.dto.response.quizzes.QuizResponse;
import com.example.demo2.service.QuizService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@AllArgsConstructor
@RequestMapping("/api/quizzes")
@Tag(name = "Quiz API", description = "Operations related to quizzes")
public class QuizController {
    private final QuizService quizService;

    @Operation(
            summary = "Create quiz",
            description = "Returns a quiz wrapped in ApiResponse"
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Successfully create quiz",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponse.class)
                    )
            )
    })
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

    @Operation(
            summary = "Bulk Create quizzes",
            description = "Returns a quiz wrapped in ApiResponse"
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Successfully bulk create quizzes",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponse.class)
                    )
            )
    })
    @PostMapping("/api/bulk-create")
    public ResponseEntity<ApiResponse<?>> bulkCreate(
            @Valid @RequestBody BulkCreateRequest request,
            HttpServletRequest servletRequest
    ) {
        quizService.bulkCreate(request);

        return ResponseEntity.ok(ApiResponse.<QuizResponse>builder()
                .success(true)
                .message("Create Bulk Quiz successful")
                .data(null)
                .meta(new ApiResponse.Meta(LocalDateTime.now(), servletRequest.getRequestURI()))
                .build());
    }

    //update 1 quiz
    @PutMapping("/api/{quizId}/update")
    public ResponseEntity<ApiResponse<QuizResponse>> update(
        Long quizId,
        @Valid @RequestBody UpdateQuizRequest request,
        HttpServletRequest servletRequest
    ) {
        var response = quizService.update(quizId, request);

        return ResponseEntity.ok(ApiResponse.<QuizResponse>builder()
                .success(true)
                .message("Update 1 Quiz successful")
                .data(response)
                .meta(new ApiResponse.Meta(LocalDateTime.now(), servletRequest.getRequestURI()))
                .build());

    }

    //delete 1 quiz
    @DeleteMapping("/api/{quizId}/delete")
    public ResponseEntity<ApiResponse<?>> delete(
            Long quizId,
            HttpServletRequest servletRequest
    ) {
        quizService.delete(quizId);

        return ResponseEntity.ok(ApiResponse.<QuizResponse>builder()
                .success(true)
                .message("Delete 1 Quiz successful")
                .data(null)
                .meta(new ApiResponse.Meta(LocalDateTime.now(), servletRequest.getRequestURI()))
                .build());
    }

    //bulk delete quizzes
    @DeleteMapping("/api/bulk-delete")
    public ResponseEntity<ApiResponse<?>> bulkDelete(
            @Valid @RequestBody BulkDeleteRequest request,
            HttpServletRequest servletRequest
    ) {
        quizService.bulkDelete(request);

        return ResponseEntity.ok(ApiResponse.builder()
                .success(true)
                .message("Bulk Delete Quizzes successful")
                .data(null)
                .meta(new ApiResponse.Meta(LocalDateTime.now(), servletRequest.getRequestURI()))
                .build());
    }

}
