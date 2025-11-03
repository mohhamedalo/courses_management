package com.example.demo2.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class RegisterRequest {
    @NotBlank
    private String fullName;

    @NotBlank
    @Email(message = "Email không hợp lệ")
    private String email;

    @NotBlank
    private String password;
}