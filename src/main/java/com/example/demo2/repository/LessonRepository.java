package com.example.demo2.repository;

import com.example.demo2.entity.Course;
import com.example.demo2.entity.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LessonRepository extends JpaRepository<Lesson, Long> {
    boolean existsByCourseAndOrderIndex(Course course, Integer orderIndex);

    List<Lesson> findByCourseAndOrderIndexGreaterThanEqualOrderByOrderIndexAsc(
            Course course, Integer orderIndex);

    @Query("SELECT COALESCE(MAX(l.orderIndex), -1) FROM Lesson l WHERE l.course = :course")
    Optional<Integer> findMaxOrderIndexByCourse(@Param("course") Course course);
}
