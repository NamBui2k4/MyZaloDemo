package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.dto.response.AuthResponse;
import org.example.dto.request.LoginRequest;
import org.example.dto.request.RegisterRequest;
import org.example.entity.User;
import org.example.exception.UserNotFoundException;
import org.example.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthResponse register(RegisterRequest request) {

        if (userRepository.existsByPhone(request.getPhone())) {
            throw new RuntimeException("Phone already exists");
        }

        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .phone(request.getPhone())
                .avatarUrl("")
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .hideOnline(false)
                .hideLastSeen(false)
                .build();

        User saved = userRepository.save(user);

        String token = jwtService.generateToken(
                saved.getUserId(),
                saved.getPhone()
        );

        return new AuthResponse(
                token,
                saved.getUserId(),
                saved.getPhone()
        );
    }

    public AuthResponse login(LoginRequest request) {

        User user = userRepository.findByPhone(request.getPhone())
                .orElseThrow(() -> new UserNotFoundException("User not found"));
//        String encodedPassword = passwordEncoder.encode(request.getPassword())''

        if (!passwordEncoder.matches(
                request.getPassword(),
                user.getPasswordHash()
        )) {
            throw new RuntimeException("Invalid credentials");
        }

        String token = jwtService.generateToken(
                user.getUserId(),
                user.getPhone()
        );

        return new AuthResponse(
                token,
                user.getUserId(),
                user.getPhone()
        );
    }
}
