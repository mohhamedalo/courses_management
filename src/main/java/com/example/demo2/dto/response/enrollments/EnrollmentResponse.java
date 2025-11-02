package com.example.demo2.dto.response.enrollments;

import lombok.*;

import java.time.LocalDateTime;

@Getter @Setter @Builder
@AllArgsConstructor
@NoArgsConstructor
public class EnrollmentResponse {
    private Long id;
    private Long courseId;
    private Long studentId;
    private Double progress;
    private LocalDateTime enrolledAt;
    private LocalDateTime completedAt;
}
