package com.example.demo2.service;

import com.example.demo2.repository.FileRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class FileService {
    private final FileRepository fileRepository;
}
