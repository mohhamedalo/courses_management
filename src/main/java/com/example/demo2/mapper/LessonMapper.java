package com.example.demo2.mapper;

import com.example.demo2.dto.response.lessons.LessonResponse;
import com.example.demo2.entity.Lesson;

public class LessonMapper {

    public static LessonResponse toResponse(Lesson l) {
        return LessonResponse.builder()
                .id(l.getId())
                .courseId(l.getCourse().getId()) // optional
                .title(l.getTitle())
                .content(l.getContent())
                .videoUrl(l.getVideoUrl())
                .orderIndex(l.getOrderIndex())
                .createdAt(l.getCreatedAt())
                .build();
    }
}
