package com.example.demo2.dto.request.enrollments;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FilterEnrollmentRequest {
    private String courseId;
    private String studentId;
    private String courseNameContains;
    private String studentNameContains;
    private Double minProgress;
    private Double maxProgress;
    private LocalDateTime enrolledFrom;
    private LocalDateTime enrolledTo;
}
