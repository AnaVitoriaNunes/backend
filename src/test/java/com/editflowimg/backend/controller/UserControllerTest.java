package com.editflowimg.backend.controller;

import com.editflowimg.backend.dto.auth.UserResponse;
import com.editflowimg.backend.entity.UserEntity;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest {

    private final UserController userController =
            new UserController();

    @Test
    void deveRetornarUsuarioLogado() {
        UUID id = UUID.randomUUID();

        UserEntity user = new UserEntity();
        user.setId(id);
        user.setName("Ana");
        user.setEmail("ana@email.com");

        UserResponse response = userController.me(user);

        assertNotNull(response);
        assertEquals(id, response.getId());
        assertEquals("Ana", response.getName());
        assertEquals("ana@email.com", response.getEmail());

    }
}