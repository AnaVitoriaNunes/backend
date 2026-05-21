package com.editflowimg.backend.controller;

import com.editflowimg.backend.dto.auth.AuthResponse;
import com.editflowimg.backend.dto.auth.LoginRequest;
import com.editflowimg.backend.dto.auth.RegisterRequest;
import com.editflowimg.backend.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthControllerTest {

    @Mock
    private AuthService authService;

    @InjectMocks
    private AuthController authController;

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

        AuthResponse response = new AuthResponse(
                UUID.randomUUID(),
                "Ana",
                "ana@email.com",
                "token"
        );

        when(authService.register(request)).thenReturn(response);

        AuthResponse result = authController.register(request);

        assertNotNull(result);
        assertEquals("Ana", result.getName());
        verify(authService).register(request);
    }

    @Test
    void deveFazerLogin() {
        LoginRequest request = new LoginRequest();
        request.setEmail("ana@email.com");
        request.setPassword("123456");

        AuthResponse response = new AuthResponse(
                UUID.randomUUID(),
                "Ana",
                "ana@email.com",
                "token"
        );

        when(authService.login(request)).thenReturn(response);

        AuthResponse result = authController.login(request);

        assertNotNull(result);
        assertEquals("token", result.getToken());
        verify(authService).login(request);

    }
}