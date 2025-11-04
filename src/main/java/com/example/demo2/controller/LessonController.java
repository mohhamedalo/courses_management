package com.example.demo2.controller;

import com.example.demo2.dto.request.lessons.CreateLessonRequest;
import com.example.demo2.dto.request.lessons.UpdateLessonRequest;
import com.example.demo2.dto.response.ApiResponse;
import com.example.demo2.dto.response.courses.CourseResponse;
import com.example.demo2.dto.response.lessons.LessonResponse;
import com.example.demo2.entity.Lesson;
import com.example.demo2.service.LessonService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@AllArgsConstructor
@RequestMapping("/api/lessons")
@Tag(name = "Lesson API", description = "Operations related to lessons")
public class LessonController {
    private final LessonService lessonService;

    @Operation(
            summary = "Create lesson",
            description = "Returns a lesson wrapped in ApiResponse"
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Successfully create lesson",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponse.class)
                    )
            )
    })
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

    @Operation(
            summary = "Update lesson",
            description = "Returns a lesson wrapped in ApiResponse"
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Successfully update lesson",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponse.class)
                    )
            )
    })
    @PutMapping("/{lessonId}/update")
    public ResponseEntity<ApiResponse<LessonResponse>> update(
            @Valid @PathVariable Long lessonId,
            @RequestBody UpdateLessonRequest request,
            HttpServletRequest servletRequest
    ) {
        var response = lessonService.update(lessonId, request);

        return ResponseEntity.ok(ApiResponse.<LessonResponse>builder()
                .success(true)
                .message("Create Lesson successful")
                .data(response)
                .meta(new ApiResponse.Meta(LocalDateTime.now(), servletRequest.getRequestURI()))
                .build());
    }

    //edit-ROLE_ADMIN only

    //delete-ROLE_ADMIN only
    @Operation(
            summary = "Delete lesson",
            description = "Returns a lesson wrapped in ApiResponse"
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Successfully delete lesson",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponse.class)
                    )
            )
    })
    @DeleteMapping("/api/{lessonId}/delete")
    public ResponseEntity<ApiResponse<?>> delete(
            Long lessonId,
            HttpServletRequest servletRequest
    ) {
        lessonService.delete(lessonId);

        return ResponseEntity.ok(ApiResponse.builder()
                .success(true)
                .message("Delete Lesson successful")
                .data(null)
                .meta(new ApiResponse.Meta(LocalDateTime.now(), servletRequest.getRequestURI()))
                .build());
    }
}
