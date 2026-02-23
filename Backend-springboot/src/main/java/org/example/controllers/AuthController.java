package org.example.controllers;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.RequiredArgsConstructor;
import org.example.dto.response.UserResponseDTO;
import org.example.dto.response.Views;
import org.example.dto.response.LoginResponse;
import org.example.dto.request.LoginRequest;
import org.example.dto.request.RegisterRequest;
import org.example.entity.User;
import org.example.service.AuthService;
import org.example.service.JwtService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final JwtService jwtService;

    @JsonView(Views.Minimal.class)
    @PostMapping("/register")
    public LoginResponse register(@RequestBody RegisterRequest request) {
        User user = authService.register(request);
        UserResponseDTO userResponseDTO = UserResponseDTO.builder()
                .userId(user.getUserId())
                .phone(user.getPhone())
                .build();
        String token = jwtService.generateToken(
                user.getUserId(),
                user.getPhone()
        );

        return new LoginResponse(
                token,
                userResponseDTO
        );
    }

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest request) {
        User user = authService.login(request);

        String token = jwtService.generateToken(
                user.getUserId(),
                user.getPhone()
        );

        UserResponseDTO userResponseDTO = UserResponseDTO.builder()
                .userId(user.getUserId())
                .email(user.getEmail())
                .avatarUrl(user.getAvatarUrl())
                .phone(user.getPhone())
                .name(user.getName())
                .build();
        return LoginResponse.builder()
                .userResponseDTO(userResponseDTO)
                .token(token)
                .build();
    }
}
