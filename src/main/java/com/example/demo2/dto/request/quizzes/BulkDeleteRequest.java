package com.example.demo2.dto.request.quizzes;

import lombok.*;

import java.util.List;

@Getter @Setter @Builder
@AllArgsConstructor
@NoArgsConstructor
public class BulkDeleteRequest {
    private List<Long> ids;
}
