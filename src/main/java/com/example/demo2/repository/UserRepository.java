package com.example.demo2.repository;

import com.example.demo2.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    // ✅ Tìm người dùng theo email (để đăng nhập)
    Optional<User> findByEmail(String email);

    // ✅ Kiểm tra email đã tồn tại hay chưa
    boolean existsByEmail(String email);
}
