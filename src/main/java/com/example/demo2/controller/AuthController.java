package com.example.demo2.controller;

import com.example.demo2.config.JwtUtils;
import com.example.demo2.entity.User;
import com.example.demo2.dto.request.*;
import com.example.demo2.dto.response.*;
import com.example.demo2.repository.UserRepository;
import com.example.demo2.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentication API", description = "Operations related to authentication")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthService authService;

    public AuthController(AuthenticationManager authenticationManager, JwtUtils jwtUtils,
                          UserRepository userRepository, PasswordEncoder passwordEncoder, AuthService authService) {
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authService = authService;
    }

    @Operation(
            summary = "Get detail authentication",
            description = "Returns a paginated list of categories wrapped in ApiResponse"
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved detail authentication",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponse.class)
                    )
            )
    })
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<String>> register(@Valid @RequestBody RegisterRequest request,
                                                        HttpServletRequest servletRequest) {
        String result = authService.register(request);
        return ResponseEntity.ok(ApiResponse.<String>builder()
                .success(true)
                .message(result)
                .data(result)
                .meta(new ApiResponse.Meta(LocalDateTime.now(), servletRequest.getRequestURI()))
                .build());
    }

    @Operation(
            summary = "Login API",
            description = "Returns data login wrapped in ApiResponse"
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Successfully login",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponse.class)
                    )
            )
    })
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@Valid @RequestBody LoginRequest request,
                                                           HttpServletRequest servletRequest) {
        AuthResponse response = authService.login(request);
        return ResponseEntity.ok(ApiResponse.<AuthResponse>builder()
                .success(true)
                .message("Login successful")
                .data(response)
                .meta(new ApiResponse.Meta(LocalDateTime.now(), servletRequest.getRequestURI()))
                .build());
    }

    @Operation(
            summary = "Refresh token",
            description = "Returns login information wrapped in ApiResponse"
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Successfully get token",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponse.class)
                    )
            )
    })
    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<AuthResponse>> refreshToken(
            @RequestBody Map<String, String> request, HttpServletRequest servletRequest) {

        String refreshToken = request.get("refreshToken");
        AuthResponse newTokens = authService.refreshToken(refreshToken);

        return ResponseEntity.ok(ApiResponse.<AuthResponse>builder()
                .success(true)
                .message("Token refreshed successfully")
                .data(newTokens)
                .meta(new ApiResponse.Meta(LocalDateTime.now(), servletRequest.getRequestURI()))
                .build());
    }

    @Operation(
            summary = "Logout",
            description = "Returns data wrapped in ApiResponse"
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Successfully logout",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponse.class)
                    )
            )
    })
    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<String>> logout(@RequestParam String email, HttpServletRequest servletRequest) {
        String result = authService.logout(email);
        return ResponseEntity.ok(ApiResponse.<String>builder()
                .success(true)
                .message(result)
                .data(result)
                .meta(new ApiResponse.Meta(LocalDateTime.now(), servletRequest.getRequestURI()))
                .build());
    }
}