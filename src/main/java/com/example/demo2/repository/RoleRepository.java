package com.example.demo2.repository;

import com.example.demo2.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    // ✅ Tìm role theo tên
    Optional<Role> findByName(String name);
}
