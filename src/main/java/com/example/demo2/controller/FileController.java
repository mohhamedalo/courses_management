package com.example.demo2.controller;

import com.example.demo2.dto.request.files.FileUploadRequest;
import com.example.demo2.dto.response.ApiResponse;
import com.example.demo2.service.FileService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.time.LocalDateTime;

@RestController
@AllArgsConstructor
@RequestMapping("/api/files")
public class FileController {
    private final FileService fileService;

    @PostMapping("/upload-multiple")
    public ResponseEntity<ApiResponse<?>> uploadMultipleFiles(
            @Valid FileUploadRequest request,
            HttpServletRequest servletRequest) throws IOException {

        var response = fileService.uploadFiles(
                request.getFiles(),
                request.getRelatedType(),
                request.getRelatedId()
        );

        return ResponseEntity.ok(ApiResponse.builder()
                .success(true)
                .message("Upload files thành công")
                .data(response)
                .meta(new ApiResponse.Meta(LocalDateTime.now(), servletRequest.getRequestURI()))
                .build());
    }
}
