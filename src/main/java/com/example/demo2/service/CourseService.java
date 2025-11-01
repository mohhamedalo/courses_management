package com.example.demo2.service;

import com.example.demo2.dto.request.course.FilterCourseRequest;
import com.example.demo2.dto.request.course.UpdateCourseRequest;
import com.example.demo2.dto.response.courses.CourseResponse;
import com.example.demo2.entity.Course;
import com.example.demo2.exception.ResourceNotFoundException;
import com.example.demo2.mapper.CategoryMapper;
import com.example.demo2.mapper.CourseMapper;
import com.example.demo2.util.SecurityUtils;
import com.example.demo2.dto.request.course.CreateCourseRequest;
import com.example.demo2.entity.Category;
import com.example.demo2.entity.User;
import com.example.demo2.repository.CourseRepository;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import jakarta.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
@Builder
public class CourseService {
    private final CourseRepository courseRepository;
    private final CategoryService categoryService;
    private final AuthService authService;
//    private final SecurityUtils securityUtils;

    public CourseResponse create(CreateCourseRequest request) {
        User loginUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = loginUser.getId();

//        Long userId = SecurityUtils.getCurrentUserId();
        Category c = categoryService.findOneById(Long.valueOf(request.getCategoryId()));
        User instructor = authService.findOneById(userId);

        Course newCourse = Course.builder()
                .description(request.getDescription())
                .level(request.getLevel())
                .price(request.getPrice())
                .status(request.getStatus())
                .thumbnailUrl(request.getThumbnailUrl())
                .title(request.getTitle())
                .category(c)
                .instructor(instructor)
                .build();

        newCourse = courseRepository.save(newCourse);

        return CourseMapper.toResponse(newCourse);
    }

    public CourseResponse update(Long courseId, UpdateCourseRequest request) {
        Course course = courseRepository
                .findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("course not found: " + courseId));

        // 2. ---- OPTIONAL SECURITY CHECK ----
        // Only the instructor (or admin) may edit the course
        Long currentUserId = SecurityUtils.getCurrentUserId();
        if (!course.getInstructor().getId().equals(currentUserId)) {
            throw new SecurityException("You are not allowed to edit this course");
        }

        // 3. ---- MERGE ONLY PROVIDED FIELDS ----
        if (request.getTitle() != null && !request.getTitle().isBlank()) {
            course.setTitle(request.getTitle().trim());
        }
        if (request.getDescription() != null && !request.getDescription().isBlank()) {
            course.setDescription(request.getDescription().trim());
        }
        if (request.getLevel() != null && !request.getLevel().isBlank()) {
            course.setLevel(request.getLevel().trim());
        }
        if (request.getPrice() != null) {
            course.setPrice(request.getPrice());
        }
        if (request.getStatus() != null && !request.getStatus().isBlank()) {
            course.setStatus(request.getStatus().trim());
        }
        if (request.getThumbnailUrl() != null && !request.getThumbnailUrl().isBlank()) {
            course.setThumbnailUrl(request.getThumbnailUrl().trim());
        }
        if (request.getCategoryId() != null) {
            Category category = categoryService.findOneById(request.getCategoryId().longValue());
            course.setCategory(category);
        }

        // 4. Persist
        Course saved = courseRepository.save(course);

        // 5. Return DTO
        return CourseMapper.toResponse(saved);
    }

    public void delete(Long courseId) {
        Course course = courseRepository
                .findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("course not found: " + courseId));

        courseRepository.delete(course);
    }

    public Page<CourseResponse> getAll(FilterCourseRequest filter, Pageable pageable) {
        Specification<Course> spec = buildSpecification(filter);
        Page<Course> page = courseRepository.findAll(spec, pageable);
        return page.map(CourseMapper::toResponse);
    }

    private Specification<Course> buildSpecification(FilterCourseRequest f) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            // LIKE description (case-insensitive)
            if (StringUtils.hasText(f.getDescriptionContains())) {
                predicates.add(cb.like(cb.lower(root.get("description")),
                        "%" + f.getDescriptionContains().toLowerCase() + "%"));
            }

            // LIKE title
            if (StringUtils.hasText(f.getTitleContains())) {
                predicates.add(cb.like(cb.lower(root.get("title")),
                        "%" + f.getTitleContains().toLowerCase() + "%"));
            }

            // IN level
            if (f.getLevels() != null && !f.getLevels().isEmpty()) {
                predicates.add(root.get("level").in(f.getLevels()));
            }

            // IN status
            if (f.getStatuses() != null && !f.getStatuses().isEmpty()) {
                predicates.add(root.get("status").in(f.getStatuses()));
            }

            // BETWEEN createdAt
            if (f.getCreatedFrom() != null && f.getCreatedTo() != null) {
                predicates.add(cb.between(root.get("createdAt"), f.getCreatedFrom(), f.getCreatedTo()));
            } else if (f.getCreatedFrom() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("createdAt"), f.getCreatedFrom()));
            } else if (f.getCreatedTo() != null) {
                predicates.add(cb.lessThan(root.get("createdAt"), f.getCreatedTo()));
            }

            // JOIN category + LIKE category.name
            if (StringUtils.hasText(f.getCategoryNameContains())) {
                Join<Course, Category> categoryJoin = root.join("category", JoinType.LEFT);
                predicates.add(cb.like(cb.lower(categoryJoin.get("name")),
                        "%" + f.getCategoryNameContains().toLowerCase() + "%"));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    public Course findOne(Long courseId) {
        return courseRepository
                .findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("course not found: " + courseId));
    }
}
