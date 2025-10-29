package com.example.demo2.service;

import com.example.demo2.config.JwtUtils;
import com.example.demo2.entity.RefreshToken;
import com.example.demo2.entity.Role;
import com.example.demo2.entity.User;
import com.example.demo2.dto.request.*;
import com.example.demo2.dto.response.*;
import com.example.demo2.exception.ResourceNotFoundException;
import com.example.demo2.repository.RoleRepository;
import com.example.demo2.repository.UserRepository;
import com.example.demo2.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final RefreshTokenService refreshTokenService;

    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        String token = jwtUtils.generateJwtToken(user);
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user);

        return AuthResponse.builder()
                .token(token)
                .refreshToken(refreshToken.getToken())
                .email(user.getEmail())
                .roles(user.getRoles().stream().map(Role::getName).toList())
                .build();
    }

    public String register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already registered!");
        }

        Role defaultRole = roleRepository.findByName("ROLE_STUDENT")
                .orElseThrow(() -> new RuntimeException("Default role not found!"));

        User user = User.builder()
                .fullName(request.getFullName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .roles(Collections.singleton(defaultRole))
                .build();

        userRepository.save(user);
        return "User registered successfully!";
    }

    public AuthResponse refreshToken(String requestToken) {
        RefreshToken refreshToken = refreshTokenService.findByToken(requestToken)
                .map(refreshTokenService::verifyExpiration)
                .orElseThrow(() -> new RuntimeException("Invalid refresh token"));

        User user = refreshToken.getUser();
        String newAccessToken = jwtUtils.generateJwtToken(user);

        return AuthResponse.builder()
                .token(newAccessToken)
                .refreshToken(refreshToken.getToken())
                .email(user.getEmail())
                .roles(user.getRoles().stream().map(Role::getName).toList())
                .build();
    }

    public String logout(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        refreshTokenService.deleteByUser(user);
        return "Logged out successfully";
    }

    public User findOneById(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found " + userId));
    }
}