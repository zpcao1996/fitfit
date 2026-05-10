package com.fitfit.research.service;

import com.fitfit.research.dto.AuthResponse;
import com.fitfit.research.dto.LoginRequest;
import com.fitfit.research.dto.RegisterRequest;
import com.fitfit.research.entity.User;
import com.fitfit.research.repository.UserRepository;
import com.fitfit.research.util.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByUsername(request.username())
                .orElseThrow(() -> new RuntimeException("用户名或密码错误"));
        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new RuntimeException("用户名或密码错误");
        }
        String token = jwtUtil.generateToken(user.getUsername(), user.isAdmin());
        return new AuthResponse(token, user.getUsername(), user.getFullName(), user.isAdmin());
    }

    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.username())) {
            throw new RuntimeException("用户名已存在");
        }
        User user = new User();
        user.setUsername(request.username());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setFullName(request.fullName());
        user.setEmail(request.email());
        userRepository.save(user);
        String token = jwtUtil.generateToken(user.getUsername(), user.isAdmin());
        return new AuthResponse(token, user.getUsername(), user.getFullName(), user.isAdmin());
    }
}
