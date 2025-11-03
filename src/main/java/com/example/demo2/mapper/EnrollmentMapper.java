package com.example.demo2.mapper;

import com.example.demo2.dto.response.enrollments.EnrollmentResponse;
import com.example.demo2.entity.Enrollment;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public class EnrollmentMapper {
    public static EnrollmentResponse toResponse(Enrollment enrollment) {
        return EnrollmentResponse.builder()
                .id(enrollment.getId())
                .courseId(enrollment.getCourse().getId())
                .studentId(enrollment.getStudent().getId())
                .progress(0.0)
                .enrolledAt(LocalDateTime.now())
                .completedAt(null)
                .build();
    }
}
