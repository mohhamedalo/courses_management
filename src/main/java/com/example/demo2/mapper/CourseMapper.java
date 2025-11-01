package com.example.demo2.mapper;

import com.example.demo2.dto.response.CategoryResponse;
import com.example.demo2.dto.response.courses.CourseResponse;
import com.example.demo2.dto.response.lessons.LessonResponse;
import com.example.demo2.entity.Course;

import java.util.Collections;
import java.util.Comparator;

public class CourseMapper {
    public static CourseResponse toResponse(Course c) {
        return CourseResponse.builder()
                .id(c.getId())
                .title(c.getTitle())
                .description(c.getDescription())
                .price(c.getPrice())
                .level(c.getLevel())
                .status(c.getStatus())
                .thumbnailUrl(c.getThumbnailUrl())
                .createdAt(c.getCreatedAt())
                .updatedAt(c.getUpdatedAt())
                .category(CategoryResponse.builder()
                        .id(c.getCategory().getId())
                        .name(c.getCategory().getName())
                        .description(c.getCategory().getDescription())
                        .build())
                .lessons(c.getLessons() == null || c.getLessons().isEmpty()
                        ? Collections.emptyList()
                        : c.getLessons().stream()
                        .map(LessonMapper::toResponse)
                        .sorted(Comparator.comparing(LessonResponse::getOrderIndex)) // optional
                        .toList())
                .instructorId(c.getInstructor().getId())
                .build();

    }
}

//private Long id;
//private String description;
//private String level;
//private Integer price;
//private String status;
//private String thumbnailUrl;
//private String title;
//private Locale createdAt;
//private Locale updatedAt;
//private Integer categoryId;
//private Integer instructorId;
