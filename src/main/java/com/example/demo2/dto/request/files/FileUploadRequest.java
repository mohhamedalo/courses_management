package com.example.demo2.dto.request.files;

import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter @Setter @Builder
@AllArgsConstructor
@NoArgsConstructor
public class FileUploadRequest {
    @NotNull(message = "File không được để trống")
    private List<MultipartFile> files;  // Nhiều file

    @NotBlank(message = "relatedType không được để trống")
    private String relatedType;         // COURSE hoặc LESSON

    @NotNull(message = "relatedId không được để trống")
    private Long relatedId;
}
