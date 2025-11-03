package com.example.demo2.dto.response;

import lombok.*;

import lombok.*;
import java.util.List;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class AuthResponse {
    private String token;
    private String refreshToken;
    private String email;
    private List<String> roles;
}