package com.example.demo2.controller;

import com.example.demo2.dto.response.CategoryResponse;
import com.example.demo2.entity.Category;
import com.example.demo2.entity.Course;
import com.example.demo2.entity.User;
import com.example.demo2.repository.CategoryRepository;
import com.example.demo2.repository.CourseRepository;
import com.example.demo2.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest // Load toàn bộ Spring context (giống chạy app thật)
@AutoConfigureMockMvc // Tự động config MockMvc để test API
@ActiveProfiles("test") // Sử dụng application-test.properties
@Transactional // Tự động rollback sau mỗi test (không làm bẩn database)
public class CourseControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private Category category;

    private User user;

    @BeforeEach
    void setUp() {
        // Xóa dữ liệu cũ
        courseRepository.deleteAll();
        categoryRepository.deleteAll();

        //
        user = User.builder()
                .fullName("hari")
                .email("hari@gmail.com")
                .password("typeyourencryptpassword")
                .build();
        userRepository.save(user);

        // Tạo dữ liệu test
        category = Category.builder()
                .name("Programming")
                .build();
        categoryRepository.save(category);

        Course course1 = Course.builder()
                .title("Java Spring Boot Complete")
                .description("Learn Spring Boot from scratch")
                .level("Beginner")
                .status("published")
                .category(category)
                .instructor(user)
                .createdAt(LocalDateTime.now())
                .build();

        Course course2 = Course.builder()
                .title("Advanced Java Patterns")
                .description("Design patterns in Java")
                .level("Advance")
                .status("draft")
                .category(category)
                .instructor(user)
                .createdAt(LocalDateTime.now())
                .build();

        courseRepository.saveAll(List.of(course1, course2));
    }

    @Test
    @DisplayName("GET /api/courses - Lấy tất cả courses thành công")
    void testGetAllCourses_Success() throws Exception {
        mockMvc.perform(get("/api/courses") // Gửi GET request
                        .header("Authorization", "Bearer eyJhbGciOiJIUzUxMiJ9.")
                        .param("page", "0")
                        .param("size", "20")
                        .param("sort", "id,desc")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()) // Expect HTTP 200
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Get List Course successful"))
                .andExpect(jsonPath("$.data.content", hasSize(2))) // Có 2 courses
                .andExpect(jsonPath("$.data.totalElements").value(2))
                .andExpect(jsonPath("$.data.content[0].title", containsString("Java")));
    }
}
