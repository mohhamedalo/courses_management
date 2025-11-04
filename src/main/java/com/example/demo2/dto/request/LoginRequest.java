package com.example.demo2.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class LoginRequest {
    @NotBlank
    @Email(message = "Email không hợp lệ")
    private String email;

    @NotBlank
    private String password;
}