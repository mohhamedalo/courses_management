package com.example.demo2.controller;

import com.example.demo2.dto.request.course.CreateCourseRequest;
import com.example.demo2.dto.request.course.FilterCourseRequest;
import com.example.demo2.dto.request.course.UpdateCourseRequest;
import com.example.demo2.dto.response.ApiResponse;
import com.example.demo2.dto.response.CategoryResponse;
import com.example.demo2.dto.response.courses.CourseResponse;
import com.example.demo2.service.CourseService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Builder;
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
@Builder
@RequestMapping("/api/courses")
public class CourseController {
    private final CourseService courseService;

    @PostMapping
    public ResponseEntity<ApiResponse<?>> create(
            @Valid @RequestBody CreateCourseRequest request,
            HttpServletRequest servletRequest
    ) {
        var response = courseService.create(request);

        return ResponseEntity.ok(ApiResponse.<CourseResponse>builder()
                .success(true)
                .message("Create Course successful")
                .data(response)
                .meta(new ApiResponse.Meta(LocalDateTime.now(), servletRequest.getRequestURI()))
                .build());
    }

    @PutMapping("/{courseId}/update")
    public ResponseEntity<ApiResponse<?>> update(
            @PathVariable Long courseId,
            @Valid @RequestBody UpdateCourseRequest request,
            HttpServletRequest servletRequest
    ) {
        var response = courseService.update(courseId, request);

        return ResponseEntity.ok(ApiResponse.<CourseResponse>builder()
                .success(true)
                .message("Update Course successful")
                .data(response)
                .meta(new ApiResponse.Meta(LocalDateTime.now(), servletRequest.getRequestURI()))
                .build());
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<CourseResponse>>> getAll(
            @Valid FilterCourseRequest filter,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id,desc") String sort,
            HttpServletRequest servletRequest
    ) {
        Pageable pageable = buildPageable(page, size, sort);
        var result = courseService.getAll(filter, pageable);

        return ResponseEntity.ok(ApiResponse.<Page<CourseResponse>>builder()
                .success(true)
                .message("Get List Course successful")
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



    @DeleteMapping("/{courseId}/delete")
    public ResponseEntity<ApiResponse<?>> delete(@PathVariable Long courseId, HttpServletRequest servletRequest) {
        courseService.delete(courseId);

        return ResponseEntity.ok(ApiResponse.builder()
                .success(true)
                .message("Delete course successfully")
                .data(null)
                .meta(new ApiResponse.Meta(LocalDateTime.now(), servletRequest.getRequestURI()))
                .build());

    }
}
