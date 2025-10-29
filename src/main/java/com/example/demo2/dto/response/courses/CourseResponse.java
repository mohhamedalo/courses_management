package com.example.demo2.dto.response.courses;

import com.example.demo2.dto.response.CategoryResponse;
import com.example.demo2.dto.response.lessons.LessonResponse;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CourseResponse {
    private Long id;
    private String description;
    private String level;
    private BigDecimal price;
    private String status;
    private String thumbnailUrl;
    private String title;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private CategoryResponse category;
    private List<LessonResponse> lessons;
    private Long instructorId;
}