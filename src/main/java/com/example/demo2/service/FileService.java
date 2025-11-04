package com.example.demo2.service;

import com.example.demo2.dto.response.files.FileResponse;
import com.example.demo2.entity.File;
import com.example.demo2.repository.FileRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileService {
    private final FileRepository fileRepository;

    @Value("${app.upload.dir}")
    private String uploadDir;

    public List<FileResponse> uploadFiles(
            List<MultipartFile> files,
            String relatedType,
            Long relatedId) throws IOException {

        List<FileResponse> results = new ArrayList<>();
        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        for (MultipartFile file : files) {
            if (file.isEmpty()) continue;

            String originalName = file.getOriginalFilename();
            String ext = getFileExtension(originalName);
            String uniqueName = UUID.randomUUID() + "." + ext;
            Path filePath = uploadPath.resolve(uniqueName);

            Files.copy(file.getInputStream(), filePath);

            File entity = File.builder()
                    .relatedType(relatedType)
                    .relatedId(relatedId)
                    .fileName(originalName)
                    .fileUrl(uploadPath + "/" + uniqueName)
                    .fileType(ext)
                    .build();

            File saved = fileRepository.save(entity);

            results.add(FileResponse.fromEntity(saved));
        }

        return results;
    }

    // Helper: Lấy đuôi file (.pdf, .mp4...)
    private String getFileExtension(String fileName) {
        if (fileName == null) return "";
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }
}
