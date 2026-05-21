package com.editflowimg.backend.controller;

import com.editflowimg.backend.dto.generation.GenerationRequest;
import com.editflowimg.backend.entity.GenerationEntity;
import com.editflowimg.backend.entity.UserEntity;
import com.editflowimg.backend.service.GenerationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GenerationControllerTest {

    @Mock
    private GenerationService generationService;

    @InjectMocks
    private GenerationController generationController;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void deveCriarGeracao() {
        GenerationRequest request = new GenerationRequest();

        UserEntity user = new UserEntity();
        user.setId(UUID.randomUUID());

        GenerationEntity generation = new GenerationEntity();
        generation.setId(UUID.randomUUID());

        when(generationService.createGeneration(request, user))
                .thenReturn(generation);

        GenerationEntity result =
                generationController.create(request, user);

        assertNotNull(result);
        verify(generationService).createGeneration(request, user);
    }

    @Test
    void deveBuscarGeracaoPorId() {
        UUID id = UUID.randomUUID();

        UserEntity user = new UserEntity();
        user.setId(UUID.randomUUID());

        GenerationEntity generation = new GenerationEntity();
        generation.setId(id);

        when(generationService.findById(id, user))
                .thenReturn(generation);

        GenerationEntity result =
                generationController.findById(id, user);

        assertEquals(id, result.getId());
        verify(generationService).findById(id, user);
    }

    @Test
    void deveListarGeracoesDoUsuario() {
        UserEntity user = new UserEntity();
        user.setId(UUID.randomUUID());

        List<GenerationEntity> generations = List.of(
                new GenerationEntity(),
                new GenerationEntity()
        );

        when(generationService.listByUser(user))
                .thenReturn(generations);

        List<GenerationEntity> result =
                generationController.listByUser(user);

        assertEquals(2, result.size());
        verify(generationService).listByUser(user);

    }
}