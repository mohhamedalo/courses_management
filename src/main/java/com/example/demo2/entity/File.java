package com.example.demo2.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "files")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class File {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String relatedType; // COURSE or LESSON
    private Long relatedId;

    private String fileName;
    private String fileUrl;
    private String fileType; // pdf, mp4, zip, ...

    @CreationTimestamp
    private LocalDateTime uploadedAt;
}