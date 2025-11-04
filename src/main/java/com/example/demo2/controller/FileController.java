package com.example.demo2.controller;

import com.example.demo2.dto.common.response.ErrorResponse;
import com.example.demo2.dto.request.files.UploadFileRequest;
import com.example.demo2.dto.response.ApiResponse;
import com.example.demo2.entity.File;
import com.example.demo2.service.FileService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/files")
@AllArgsConstructor
public class FileController {
    private final FileService fileService;

    @PostMapping("/api/upload")
    public ResponseEntity<ApiResponse<File>> uploadFile(
            UploadFileRequest request,
            HttpServletRequest servletRequest
    ) {
        try {
            File savedFile = fileService.uploadFile(request);
            return FileMapper.toResponse(savedFile);

        } catch (IOException e) {
            return ResponseEntity.badRequest().build();
        }

    }
}
