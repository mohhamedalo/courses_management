package com.example.demo2.dto.common.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse<T> {
    private boolean success;
    private String message;
    private T data;
    private Object error;
    private com.example.demo2.dto.common.response.ErrorResponse.Meta meta;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Meta {
        private LocalDateTime timestamp;
        private String path;
    }
}