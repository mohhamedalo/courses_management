-- ==================== BẢNG ROLES ====================
CREATE TABLE roles (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE
);
COMMENT ON TABLE roles IS 'Bảng lưu các vai trò người dùng';
COMMENT ON COLUMN roles.name IS 'Tên vai trò: ROLE_ADMIN, ROLE_INSTRUCTOR, ROLE_STUDENT';

-- ==================== BẢNG USERS ====================
CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    full_name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    avatar_url VARCHAR(255),
    bio TEXT,
    created_at TIMESTAMP DEFAULT NOW(),
    updated_at TIMESTAMP DEFAULT NOW()
);
COMMENT ON TABLE users IS 'Bảng lưu thông tin người dùng';
COMMENT ON COLUMN users.full_name IS 'Họ và tên';
COMMENT ON COLUMN users.email IS 'Email đăng nhập, duy nhất';
COMMENT ON COLUMN users.password IS 'Mật khẩu đã mã hóa';
COMMENT ON COLUMN users.avatar_url IS 'Link ảnh đại diện';
COMMENT ON COLUMN users.bio IS 'Tiểu sử người dùng';
COMMENT ON COLUMN users.created_at IS 'Thời gian tạo';
COMMENT ON COLUMN users.updated_at IS 'Thời gian cập nhật';

-- Trigger cập nhật updated_at
CREATE OR REPLACE FUNCTION update_updated_at()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = NOW();
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_users_update
    BEFORE UPDATE ON users
    FOR EACH ROW
    EXECUTE FUNCTION update_updated_at();

-- ==================== BẢNG USER_ROLES (N:N) ====================
CREATE TABLE user_roles (
    user_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,
    PRIMARY KEY (user_id, role_id),
    CONSTRAINT fk_user_roles_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT fk_user_roles_role FOREIGN KEY (role_id) REFERENCES roles(id) ON DELETE CASCADE
);
COMMENT ON TABLE user_roles IS 'Bảng trung gian liên kết người dùng và vai trò';

-- ==================== BẢNG CATEGORIES ====================
CREATE TABLE categories (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    description TEXT,
    created_at TIMESTAMP DEFAULT NOW()
);
COMMENT ON TABLE categories IS 'Bảng lưu danh mục khóa học';
COMMENT ON COLUMN categories.name IS 'Tên danh mục, duy nhất';

-- ==================== BẢNG COURSES ====================
CREATE TABLE courses (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(200) NOT NULL,
    description TEXT,
    price NUMERIC(10,2),
    level VARCHAR(50),
    status VARCHAR(20),
    thumbnail_url VARCHAR(255),
    category_id BIGINT,
    instructor_id BIGINT,
    created_at TIMESTAMP DEFAULT NOW(),
    updated_at TIMESTAMP DEFAULT NOW(),

    CONSTRAINT fk_courses_category FOREIGN KEY (category_id) REFERENCES categories(id) ON DELETE SET NULL,
    CONSTRAINT fk_courses_instructor FOREIGN KEY (instructor_id) REFERENCES users(id) ON DELETE SET NULL
);
COMMENT ON TABLE courses IS 'Bảng lưu thông tin khóa học';
CREATE TRIGGER trg_courses_update
    BEFORE UPDATE ON courses
    FOR EACH ROW
    EXECUTE FUNCTION update_updated_at();

-- ==================== BẢNG LESSONS ====================
CREATE TABLE lessons (
    id BIGSERIAL PRIMARY KEY,
    course_id BIGINT NOT NULL,
    title VARCHAR(200) NOT NULL,
    content TEXT,
    video_url VARCHAR(255),
    order_index INT,
    created_at TIMESTAMP DEFAULT NOW(),

    CONSTRAINT fk_lessons_course FOREIGN KEY (course_id) REFERENCES courses(id) ON DELETE CASCADE
);
COMMENT ON TABLE lessons IS 'Bảng lưu bài học trong khóa học';

-- ==================== BẢNG QUIZZES ====================
CREATE TABLE quizzes (
    id BIGSERIAL PRIMARY KEY,
    lesson_id BIGINT NOT NULL,
    question TEXT NOT NULL,
    option_a VARCHAR(255),
    option_b VARCHAR(255),
    option_c VARCHAR(255),
    option_d VARCHAR(255),
    correct_answer VARCHAR(1),

    CONSTRAINT fk_quizzes_lesson FOREIGN KEY (lesson_id) REFERENCES lessons(id) ON DELETE CASCADE
);
COMMENT ON TABLE quizzes IS 'Bảng lưu câu hỏi trắc nghiệm';

-- ==================== BẢNG QUIZ_ANSWERS ====================
CREATE TABLE quiz_answers (
    id BIGSERIAL PRIMARY KEY,
    quiz_id BIGINT NOT NULL,
    student_id BIGINT NOT NULL,
    selected_answer VARCHAR(255),
    is_correct BOOLEAN,
    answered_at TIMESTAMP DEFAULT NOW(),

    CONSTRAINT fk_quiz_answers_quiz FOREIGN KEY (quiz_id) REFERENCES quizzes(id) ON DELETE CASCADE,
    CONSTRAINT fk_quiz_answers_student FOREIGN KEY (student_id) REFERENCES users(id) ON DELETE CASCADE
);
COMMENT ON TABLE quiz_answers IS 'Bảng lưu câu trả lời của học viên';

-- ==================== BẢNG REVIEWS ====================
CREATE TABLE reviews (
    id BIGSERIAL PRIMARY KEY,
    course_id BIGINT NOT NULL,
    student_id BIGINT NOT NULL,
    rating INT CHECK (rating BETWEEN 1 AND 5),
    comment TEXT,
    created_at TIMESTAMP DEFAULT NOW(),

    CONSTRAINT fk_reviews_course FOREIGN KEY (course_id) REFERENCES courses(id) ON DELETE CASCADE,
    CONSTRAINT fk_reviews_student FOREIGN KEY (student_id) REFERENCES users(id) ON DELETE CASCADE
);
COMMENT ON TABLE reviews IS 'Bảng lưu đánh giá khóa học';

-- ==================== BẢNG ENROLLMENTS ====================
CREATE TABLE enrollments (
    id BIGSERIAL PRIMARY KEY,
    course_id BIGINT NOT NULL,
    student_id BIGINT NOT NULL,
    progress DOUBLE PRECISION,
    enrolled_at TIMESTAMP DEFAULT NOW(),
    completed_at TIMESTAMP,

    CONSTRAINT fk_enrollments_course FOREIGN KEY (course_id) REFERENCES courses(id) ON DELETE CASCADE,
    CONSTRAINT fk_enrollments_student FOREIGN KEY (student_id) REFERENCES users(id) ON DELETE CASCADE
);
COMMENT ON TABLE enrollments IS 'Bảng lưu thông tin đăng ký học';

-- ==================== BẢNG REFRESH_TOKENS ====================
CREATE TABLE refresh_tokens (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL UNIQUE,
    token VARCHAR(500) NOT NULL UNIQUE,
    expiry_date TIMESTAMP NOT NULL,

    CONSTRAINT fk_refresh_tokens_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);
COMMENT ON TABLE refresh_tokens IS 'Bảng lưu refresh token cho đăng nhập';

-- ==================== BẢNG FILES ====================
CREATE TABLE files (
    id BIGSERIAL PRIMARY KEY,
    related_type VARCHAR(20) NOT NULL,
    related_id BIGINT NOT NULL,
    file_name VARCHAR(255) NOT NULL,
    file_url VARCHAR(500) NOT NULL,
    file_type VARCHAR(50) NOT NULL,
    uploaded_at TIMESTAMP DEFAULT NOW()
);
CREATE INDEX idx_files_related ON files (related_type, related_id);
COMMENT ON TABLE files IS 'Bảng lưu file đính kèm cho khóa học và bài học';
COMMENT ON COLUMN files.related_type IS 'Loại đối tượng: COURSE hoặc LESSON';
COMMENT ON COLUMN files.related_id IS 'ID của khóa học hoặc bài học';
COMMENT ON COLUMN files.file_name IS 'Tên file gốc';
COMMENT ON COLUMN files.file_url IS 'Link tải file (S3, Cloudinary, v.v.)';
COMMENT ON COLUMN files.file_type IS 'Kiểu file: pdf, mp4, zip, docx, jpg...';