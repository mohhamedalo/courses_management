package com.example.demo2.repository;

import com.example.demo2.entity.Course;
import com.example.demo2.entity.Enrollment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, Long>, JpaSpecificationExecutor<Enrollment> {
    @Query("SELECT e FROM Enrollment e WHERE e.id = :id AND e.student.id = :studentId")
    Optional<Enrollment> findByIdAndStudentId(@Param("id") Long id, @Param("studentId") Long studentId);
}
