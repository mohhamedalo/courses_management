package com.example.demo2.service;

import com.example.demo2.dto.request.enrollments.EnrollmentRequest;
import com.example.demo2.dto.request.enrollments.FilterEnrollmentRequest;
import com.example.demo2.dto.response.enrollments.EnrollmentResponse;
import com.example.demo2.entity.Course;
import com.example.demo2.entity.Enrollment;
import com.example.demo2.entity.User;
import com.example.demo2.exception.ResourceNotFoundException;
import com.example.demo2.mapper.EnrollmentMapper;
import com.example.demo2.repository.EnrollmentRepository;
import com.example.demo2.repository.RoleRepository;
import com.example.demo2.repository.UserRepository;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
@Builder
public class EnrollmentService {
    private final EnrollmentRepository enrollmentRepository;
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
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

    public Page<EnrollmentResponse> getAll(FilterEnrollmentRequest filter, Pageable pageable) {
        Specification<Enrollment> spec = buildSpecification(filter);
        Page<Enrollment> page = enrollmentRepository.findAll(spec, pageable);
        return page.map(EnrollmentMapper::toResponse);
    }

    private Specification<Enrollment> buildSpecification(FilterEnrollmentRequest f) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            User loginUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            Long userId = loginUser.getId();

            User user = userRepository.findById(userId).orElseThrow(()-> new ResourceNotFoundException("user not found: " + userId));

            boolean isAdmin = user.getRoles()
                    .stream()
                    .anyMatch(role -> "ROLE_ADMIN".equals(role.getName()));

            if (!isAdmin) {
                predicates.add(cb.equal(root.get("student").get("id"), userId));
            }

            // 1. Lọc theo progress (ví dụ: > 50%)
            if (f.getMinProgress() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("progress"), f.getMinProgress()));
            }
            if (f.getMaxProgress() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("progress"), f.getMaxProgress()));
            }

            // 2. Lọc theo ngày đăng ký (enrolledAt)
            if (f.getEnrolledFrom() != null && f.getEnrolledTo() != null) {
                predicates.add(cb.between(root.get("enrolledAt"), f.getEnrolledFrom(), f.getEnrolledTo()));
            } else if (f.getEnrolledFrom() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("enrolledAt"), f.getEnrolledFrom()));
            } else if (f.getEnrolledTo() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("enrolledAt"), f.getEnrolledTo()));
            }

            // 3. Lọc theo student ID → DÙNG JOIN
            if (f.getStudentId() != null) {
                Join<Enrollment, User> studentJoin = root.join("student", JoinType.LEFT);
                Long studentId = Long.valueOf(f.getStudentId()); // Chuyển String → Long
                predicates.add(cb.equal(studentJoin.get("id"), studentId));
            }

            // 4. Lọc theo course ID → DÙNG JOIN
            if (f.getCourseId() != null) {
                Join<Enrollment, Course> courseJoin = root.join("course", JoinType.LEFT);
                predicates.add(cb.equal(courseJoin.get("id"), f.getCourseId()));
            }

            // 5. Lọc theo tên khóa học (JOIN course)
            if (StringUtils.hasText(f.getCourseNameContains())) {
                Join<Enrollment, Course> courseJoin = root.join("course", JoinType.LEFT);
                predicates.add(cb.like(cb.lower(courseJoin.get("name")),
                        "%" + f.getCourseNameContains().toLowerCase() + "%"));
            }

            // 6. Lọc theo tên sinh viên (JOIN student)
            if (StringUtils.hasText(f.getStudentNameContains())) {
                Join<Enrollment, User> studentJoin = root.join("student", JoinType.LEFT);
                predicates.add(cb.like(cb.lower(studentJoin.get("fullName")),
                        "%" + f.getStudentNameContains().toLowerCase() + "%"));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    public EnrollmentResponse findByOne(Long id) {
        // admin can see all records,
        // user only see its records
        User loginUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = loginUser.getId();

        User user = userRepository.findById(userId).orElseThrow(()-> new ResourceNotFoundException("user not found: " + userId));

        boolean isAdmin = user.getRoles()
                .stream()
                .anyMatch(role -> "ROLE_ADMIN".equals(role.getName()));

        Enrollment e;

        if (!isAdmin) {
            e = enrollmentRepository
                    .findByIdAndStudentId(id, userId)
                    .orElseThrow(() -> new ResourceNotFoundException("user not allow to see this: " + id));
        } else {
            e = enrollmentRepository
                    .findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("enrollment not found: " + id));
        }

        return EnrollmentMapper.toResponse(e);
    }
}
