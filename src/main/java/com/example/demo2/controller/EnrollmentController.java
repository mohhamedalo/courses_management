package com.example.demo2.controller;

import com.example.demo2.dto.request.course.FilterCourseRequest;
import com.example.demo2.dto.request.enrollments.EnrollmentRequest;
import com.example.demo2.dto.request.enrollments.FilterEnrollmentRequest;
import com.example.demo2.dto.response.ApiResponse;
import com.example.demo2.dto.response.courses.CourseResponse;
import com.example.demo2.dto.response.enrollments.EnrollmentResponse;
import com.example.demo2.entity.Enrollment;
import com.example.demo2.service.EnrollmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/enrollments")
@Tag(name = "Enrollment API", description = "Operations related to enrollments")
public class EnrollmentController {
    private final EnrollmentService enrollmentService;

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

    @Operation(
            summary = "Get enrollments",
            description = "Returns list enrollments wrapped in ApiResponse"
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Successfully get list enrollments",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponse.class)
                    )
            )
    })
    @GetMapping
    public ResponseEntity<ApiResponse<Page<EnrollmentResponse>>> getAll(
            @Valid FilterEnrollmentRequest filter,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id,desc") String sort,
            HttpServletRequest servletRequest
    ) {
        Pageable pageable = buildPageable(page, size, sort);
        var result = enrollmentService.getAll(filter, pageable);

        return ResponseEntity.ok(ApiResponse.<Page<EnrollmentResponse>>builder()
                .success(true)
                .message("Get List Enrollment successful")
                .data(result)
                .meta(new ApiResponse.Meta(LocalDateTime.now(), servletRequest.getRequestURI()))
                .build());
    }

    private Pageable buildPageable(int page, int size, String sort) {
        List<Sort.Order> orders = new ArrayList<>();
        // support multi-column sort: "title,asc;price,desc"
        for (String part : sort.split(";")) {
            String[] arr = part.split(",");
            String property = arr[0].trim();
            Sort.Direction direction = arr.length > 1 && "desc".equalsIgnoreCase(arr[1].trim())
                    ? Sort.Direction.DESC
                    : Sort.Direction.ASC;
            orders.add(new Sort.Order(direction, property));
        }
        return PageRequest.of(page, size, Sort.by(orders));
    }

    @Operation(
            summary = "Show enrollments",
            description = "Returns detail enrollment wrapped in ApiResponse"
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Successfully get detail enrollment",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponse.class)
                    )
            )
    })
    @GetMapping("/{id}/show")
    public ResponseEntity<ApiResponse<EnrollmentResponse>> show(
            @PathVariable Long id,
            HttpServletRequest servletRequest
    ) {
        var response = enrollmentService.findByOne(id);

        return ResponseEntity.ok(ApiResponse.<EnrollmentResponse>builder()
                .success(true)
                .message("Get detail Enrollment successful")
                .data(response)
                .meta(new ApiResponse.Meta(LocalDateTime.now(), servletRequest.getRequestURI()))
                .build());

    }

}
