package com.example.demo2.service;


import com.example.demo2.dto.request.course.FilterCourseRequest;
import com.example.demo2.dto.response.courses.CourseResponse;
import com.example.demo2.entity.Category;
import com.example.demo2.entity.Course;
import com.example.demo2.entity.User;
import com.example.demo2.repository.CourseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class) // Kích hoạt Mockito để tạo Mock objects
@DisplayName("Unit Test cho CourseService")
public class CourseServiceTest {

    @Mock // Tạo object giả lập
    private CourseRepository courseRepository;

    @InjectMocks // Tự động inject các @Mock vào đây
    private CourseService courseService;

    private FilterCourseRequest filter;
    private Pageable pageable;
    private Course course1;
    private Course course2;
    private Category category;
    private User user;

    @BeforeEach // Chạy trước mỗi test case
    void setUp() {
        // Chuẩn bị dữ liệu test
        filter = new FilterCourseRequest();
        filter.setTitleContains("Java");

        pageable = PageRequest.of(0, 20, Sort.by("id").descending());

        user = User.builder()
                .fullName("hari")
                .email("hari@gmail.com")
                .password("typeyourencryptpassword")
                .build();

        category = Category.builder()
                .name("Programming")
                .build();

        // Tạo dữ liệu mẫu
        course1 = Course.builder()
                .id(1L)
                .title("Java Spring Boot")
                .description("Learn Spring Boot")
                .level("Beginner")
                .status("published")
                .category(category)
                .instructor(user)
                .createdAt(LocalDateTime.now())
                .build();

        course2 = Course.builder()
                .id(2L)
                .title("Advanced Java")
                .description("Advanced concepts")
                .level("Advanced")
                .status("published")
                .category(category)
                .instructor(user)
                .createdAt(LocalDateTime.now())
                .build();
    }

    @Test
    @DisplayName("Test getAll - Trả về danh sách course thành công(without filter)")
    void testGetAll_Success() {
        // ARRANGE (Chuẩn bị)
        List<Course> courses = Arrays.asList(course1, course2);
        Page<Course> coursePage = new PageImpl<>(courses, pageable, courses.size());

        // Mock hành vi của repository
        when(courseRepository.findAll(any(Specification.class), any(Pageable.class)))
                .thenReturn(coursePage);

        // ACT (Thực thi)
        Page<CourseResponse> result = courseService.getAll(filter, pageable);

        // ASSERT (Kiểm tra kết quả)
        assertNotNull(result); // Kết quả không null
        assertEquals(2, result.getTotalElements()); // Có 2 courses
        assertEquals("Java Spring Boot", result.getContent().get(0).getTitle());

        // Verify rằng repository.findAll đã được gọi đúng 1 lần
        verify(courseRepository, times(1))
                .findAll(any(Specification.class), any(Pageable.class));

    }

    @Test
    @DisplayName("Test getAll - Trả về trang rỗng khi không có dữ liệu")
    void testGetAll_EmptyResult() {
        // ARRANGE
        Page<Course> emptyPage = new PageImpl<>(List.of(), pageable, 0);
        when(courseRepository.findAll(any(Specification.class), any(Pageable.class)))
                .thenReturn(emptyPage);

        // ACT
        Page<CourseResponse> result = courseService.getAll(filter, pageable);

        // ASSERT
        assertNotNull(result);
        assertEquals(0, result.getTotalElements());
        assertTrue(result.getContent().isEmpty());
    }


}
