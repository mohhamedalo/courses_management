package com.example.demo2.service;

import com.example.demo2.dto.request.lessons.CreateLessonRequest;
import com.example.demo2.dto.request.lessons.UpdateLessonRequest;
import com.example.demo2.dto.response.lessons.LessonResponse;
import com.example.demo2.entity.Course;
import com.example.demo2.entity.Lesson;
import com.example.demo2.exception.ResourceNotFoundException;
import com.example.demo2.mapper.LessonMapper;
import com.example.demo2.repository.LessonRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class LessonService {
    private final LessonRepository lessonRepository;
    private final CourseService courseService;

    public LessonResponse create(CreateLessonRequest request) {
        Course c = courseService.findOne(request.getCourseId());

        Integer orderIndex = request.getOrderIndex();

        // Nếu không gửi → chèn cuối
        if (orderIndex == null) {
            orderIndex = lessonRepository.findMaxOrderIndexByCourse(c).orElse(-1) + 1;
        } else {
            // Nếu đã tồn tại → dịch chuyển
            if (lessonRepository.existsByCourseAndOrderIndex(c, orderIndex)) {
                shiftOrderIndices(c, orderIndex);
            }
        }

        Lesson lesson = Lesson.builder()
                .content(request.getContent())
                .orderIndex(orderIndex)
                .title(request.getTitle())
                .videoUrl(request.getVideoUrl())
                .course(c)
                .build();

        lesson = lessonRepository.save(lesson);

        return LessonMapper.toResponse(lesson);
    }

    private void shiftOrderIndices(Course course, Integer startIndex) {
        List<Lesson> toShift = lessonRepository
                .findByCourseAndOrderIndexGreaterThanEqualOrderByOrderIndexAsc(course, startIndex);
        toShift.forEach(l -> l.setOrderIndex(l.getOrderIndex() + 1));
        lessonRepository.saveAll(toShift);
    }

    public Lesson findOne(Long lessonId) {
        return lessonRepository
                .findById(lessonId)
                .orElseThrow(() -> new ResourceNotFoundException("lesson not found :" + lessonId));
    }

    public LessonResponse update(Long lessonId, UpdateLessonRequest request) {
        Lesson l = this.findOne(lessonId);

        if (request.getTitle() != null && !request.getTitle().isBlank()) {
            l.setTitle(request.getTitle());
        }

        if (request.getContent() != null && !request.getContent().isBlank()) {
            l.setContent(request.getContent());
        }

        if (request.getVideoUrl() != null && !request.getVideoUrl().isBlank()) {
            l.setVideoUrl(request.getVideoUrl());
        }

        l = lessonRepository.save(l);

        return LessonMapper.toResponse(l);
    }

    public void delete(Long lessonId) {
        Lesson l = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new ResourceNotFoundException("lesson not found: " + lessonId));

        lessonRepository.delete(l);
    }
}
