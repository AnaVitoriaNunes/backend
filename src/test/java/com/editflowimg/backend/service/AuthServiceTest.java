package com.editflowimg.backend.service;

import com.editflowimg.backend.dto.auth.AuthResponse;
import com.editflowimg.backend.dto.auth.LoginRequest;
import com.editflowimg.backend.dto.auth.RegisterRequest;
import com.editflowimg.backend.entity.UserEntity;
import com.editflowimg.backend.exception.AppException;
import com.editflowimg.backend.repository.UserRepository;
import com.editflowimg.backend.security.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private AuthService authService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void deveRegistrarUsuario() {

        RegisterRequest request = new RegisterRequest();
        request.setName("Ana");
        request.setEmail("ana@email.com");
        request.setPassword("123456");

        when(userRepository.existsByEmail(request.getEmail()))
                .thenReturn(false);

        when(passwordEncoder.encode("123456"))
                .thenReturn("senha-criptografada");

        UserEntity savedUser = new UserEntity();
        savedUser.setId(UUID.randomUUID());
        savedUser.setName("Ana");
        savedUser.setEmail("ana@email.com");

        when(userRepository.save(any(UserEntity.class)))
                .thenReturn(savedUser);

        when(jwtUtil.generate(savedUser.getId()))
                .thenReturn("token-jwt");

        AuthResponse response = authService.register(request);

        assertNotNull(response);
        assertEquals("Ana", response.getName());
        assertEquals("token-jwt", response.getToken());
    }

    @Test
    void naoDeveRegistrarEmailDuplicado() {

        RegisterRequest request = new RegisterRequest();
        request.setEmail("ana@email.com");

        when(userRepository.existsByEmail(request.getEmail()))
                .thenReturn(true);

        assertThrows(AppException.class,
                () -> authService.register(request));
    }

    @Test
    void deveFazerLogin() {

        LoginRequest request = new LoginRequest();
        request.setEmail("ana@email.com");
        request.setPassword("123456");

        UserEntity user = new UserEntity();
        user.setId(UUID.randomUUID());
        user.setName("Ana");
        user.setEmail("ana@email.com");
        user.setPasswordHash("senha-criptografada");

        when(userRepository.findByEmail(request.getEmail()))
                .thenReturn(Optional.of(user));

        when(passwordEncoder.matches("123456", "senha-criptografada"))
                .thenReturn(true);

        when(jwtUtil.generate(user.getId()))
                .thenReturn("token-jwt");

        AuthResponse response = authService.login(request);

        assertNotNull(response);
        assertEquals("token-jwt", response.getToken());
    }

    @Test
    void naoDeveLogarSenhaInvalida() {

        LoginRequest request = new LoginRequest();
        request.setEmail("ana@email.com");
        request.setPassword("123456");

        UserEntity user = new UserEntity();
        user.setPasswordHash("senha-criptografada");

        when(userRepository.findByEmail(request.getEmail()))
                .thenReturn(Optional.of(user));

        when(passwordEncoder.matches(anyString(), anyString()))
                .thenReturn(false);

        assertThrows(AppException.class,
                () -> authService.login(request));

    }
}