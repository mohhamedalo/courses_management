package com.example.demo2.dto.request.course;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder

public class FilterCourseRequest {
    // LIKE (contains) – case-insensitive
    private String descriptionContains;
    private String titleContains;
    private String categoryNameContains;

    // IN (exact match)
    private List<String> levels;      // e.g. ?levels=beginner,advance
    private List<String> statuses;    // e.g. ?statuses=draft,published

    // BETWEEN on createdAt
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime createdFrom;   // >=
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime createdTo;     // < (exclusive)

    // Pagination & sorting are handled by Spring Pageable
}
