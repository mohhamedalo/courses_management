-- ==================== THÊM VAI TRÒ ====================
INSERT INTO roles (name) VALUES
('ROLE_ADMIN'), ('ROLE_INSTRUCTOR'), ('ROLE_STUDENT');

-- ==================== THÊM NGƯỜI DÙNG ====================
INSERT INTO users (full_name, email, password, avatar_url, bio) VALUES
('Admin System', 'admin@gmail.com', '$2a$10$1vLGWKSUcic5ZX9UNhQ7T.ysWRLaiVnjh5qganEoioPwvspGiiMMy', NULL, 'Quản trị viên hệ thống'),
('Giảng viên A', 'instructor@gmail.com', '$2a$10$1vLGWKSUcic5ZX9UNhQ7T.ysWRLaiVnjh5qganEoioPwvspGiiMMy', NULL, 'Chuyên gia Spring Boot'),
('Học viên Nguyễn Văn An', 'an@student.com', '$2a$10$1vLGWKSUcic5ZX9UNhQ7T.ysWRLaiVnjh5qganEoioPwvspGiiMMy', NULL, 'Học viên tích cực'),
('Học viên Trần Thị Bình', 'binh@student.com', '$2a$10$1vLGWKSUcic5ZX9UNhQ7T.ysWRLaiVnjh5qganEoioPwvspGiiMMy', NULL, 'Yêu thích lập trình');

-- Gán vai trò
INSERT INTO user_roles (user_id, role_id) VALUES
(1, 1), (2, 2), (3, 3), (4, 3);

-- ==================== THÊM DANH MỤC ====================
INSERT INTO categories (name, description) VALUES
('Lập trình Java', 'Các khóa học về Java, Spring Boot, JPA...'),
('Lập trình Web', 'HTML, CSS, JavaScript, React, Node.js...'),
('Cơ sở dữ liệu', 'MySQL, PostgreSQL, MongoDB...');

-- ==================== THÊM KHÓA HỌC ====================
INSERT INTO courses (title, description, price, level, status, thumbnail_url, category_id, instructor_id) VALUES
('Spring Boot từ A-Z', 'Học xây dựng API RESTful chuyên nghiệp với Spring Boot', 999000.00, 'Intermediate', 'published', 'https://example.com/thumb1.jpg', 1, 2),
('Java Core cho người mới', 'Nền tảng Java từ cơ bản đến nâng cao', 499000.00, 'Beginner', 'published', 'https://example.com/thumb2.jpg', 1, 2),
('MySQL Master', 'Tối ưu truy vấn, thiết kế database', 799000.00, 'Advanced', 'draft', NULL, 3, 2);

-- ==================== THÊM BÀI HỌC ====================
INSERT INTO lessons (course_id, title, content, video_url, order_index) VALUES
(1, 'Giới thiệu Spring Boot', 'Tổng quan về Spring Boot và ưu điểm', 'https://youtube.com/lesson1', 1),
(1, 'Tạo API REST', 'Xây dựng CRUD API với Spring Data JPA', 'https://youtube.com/lesson2', 2),
(2, 'Biến và kiểu dữ liệu', 'Học các kiểu dữ liệu cơ bản trong Java', 'https://youtube.com/lesson3', 1);

-- ==================== THÊM CÂU HỎI TRẮC NGHIỆM ====================
INSERT INTO quizzes (lesson_id, question, option_a, option_b, option_c, option_d, correct_answer) VALUES
(1, 'Spring Boot là gì?', 'Framework Java', 'Ngôn ngữ lập trình', 'IDE', 'Database', 'A'),
(2, 'Annotation @RestController dùng để?', 'Xử lý request HTTP', 'Kết nối database', 'Tạo bean', 'Validate', 'A');

-- ==================== THÊM ĐÁNH GIÁ ====================
INSERT INTO reviews (course_id, student_id, rating, comment) VALUES
(1, 3, 5, 'Khóa học rất hay, giảng viên nhiệt tình!'),
(1, 4, 4, 'Nội dung tốt, nhưng cần thêm ví dụ thực tế');

-- ==================== THÊM ĐĂNG KÝ HỌC ====================
INSERT INTO enrollments (course_id, student_id, progress, completed_at) VALUES
(1, 3, 65.5, NULL),
(1, 4, 30.0, NULL),
(2, 3, 100.0, '2025-10-28 10:00:00');

-- ==================== THÊM FILE MẪU ====================
INSERT INTO files (related_type, related_id, file_name, file_url, file_type) VALUES
('COURSE', 1, 'Giới thiệu Spring Boot.pdf', 'https://example.com/files/spring-intro.pdf', 'pdf'),
('COURSE', 1, 'Tài liệu tham khảo.zip', 'https://example.com/files/spring-docs.zip', 'zip'),
('COURSE', 2, 'Java Cheat Sheet.pdf', 'https://example.com/files/java-cheatsheet.pdf', 'pdf'),
('LESSON', 1, 'Slide bài 1 - Giới thiệu.pptx', 'https://example.com/files/lesson1-slides.pptx', 'pptx'),
('LESSON', 1, 'video_bai_1.mp4', 'https://example.com/videos/lesson1.mp4', 'mp4'),
('LESSON', 2, 'source_code_rest_api.zip', 'https://example.com/code/rest-api-source.zip', 'zip'),
('LESSON', 3, 'mindmap_java_core.jpg', 'https://example.com/images/java-mindmap.jpg', 'jpg');