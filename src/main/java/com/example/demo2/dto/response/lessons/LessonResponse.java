package com.example.demo2.dto.response.lessons;

import com.example.demo2.dto.response.CategoryResponse;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LessonResponse {
    private Long id;
    private Long courseId;
    private String title;
    private String content;
    private String videoUrl;
    private Integer orderIndex;
    private LocalDateTime createdAt;
}
