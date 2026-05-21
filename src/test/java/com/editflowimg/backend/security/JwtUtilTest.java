package com.editflowimg.backend.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Base64;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class JwtUtilTest {

    private JwtUtil jwtUtil;

    @BeforeEach
    void setup() {
        jwtUtil = new JwtUtil();

        String secret = Base64.getEncoder()
                .encodeToString("minha-chave-secreta-com-32-bytes".getBytes());

        ReflectionTestUtils.setField(jwtUtil, "secret", secret);
        ReflectionTestUtils.setField(jwtUtil, "expirationMs", 86400000L);
    }

    @Test
    void deveGerarTokenValido() {

        UUID userId = UUID.randomUUID();

        String token = jwtUtil.generate(userId);

        assertNotNull(token);
        assertTrue(jwtUtil.isValid(token));
    }

    @Test
    void deveExtrairUserIdDoToken() {

        UUID userId = UUID.randomUUID();

        String token = jwtUtil.generate(userId);

        UUID extracted = jwtUtil.extractUserId(token);

        assertEquals(userId, extracted);
    }

    @Test
    void deveRetornarFalseQuandoTokenForInvalido() {

        boolean result = jwtUtil.isValid("token-invalido");

        assertFalse(result);

    }
}