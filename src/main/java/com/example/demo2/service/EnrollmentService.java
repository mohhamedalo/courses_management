package com.example.demo2.service;

import com.example.demo2.dto.request.enrollments.EnrollmentRequest;
import com.example.demo2.dto.response.CategoryResponse;
import com.example.demo2.dto.response.enrollments.EnrollmentResponse;
import com.example.demo2.entity.Course;
import com.example.demo2.entity.Enrollment;
import com.example.demo2.entity.User;
import com.example.demo2.mapper.EnrollmentMapper;
import com.example.demo2.repository.EnrollmentRepository;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
@Builder
public class EnrollmentService {
    private final EnrollmentRepository enrollmentRepository;
    private final CourseService courseService;
    private final AuthService authService;

    public EnrollmentResponse create(EnrollmentRequest request) {
        User loginUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = loginUser.getId();

        Course c = courseService.findOne(Long.valueOf(request.getCourseId()));
        User u = authService.findOneById(userId);

        Enrollment newEnrollment = Enrollment.builder()
                .course(c)
                .student(u)
                .progress(0.0)
                .enrolledAt(LocalDateTime.now())
                .completedAt(null)
                .build();

        newEnrollment = enrollmentRepository.save(newEnrollment);

        return EnrollmentMapper.toResponse(newEnrollment);

    }
}
