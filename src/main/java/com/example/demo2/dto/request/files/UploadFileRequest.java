package com.example.demo2.dto.request.files;

import lombok.AllArgsConstructor;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Setter @Getter @Builder
@AllArgsConstructor
public class UploadFileRequest {
    private MultipartFile file;
    private String relatedType;
    private Long relatedId;
}
