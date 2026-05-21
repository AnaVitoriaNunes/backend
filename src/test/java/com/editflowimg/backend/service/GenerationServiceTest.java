package com.editflowimg.backend.service;

import com.editflowimg.backend.dto.generation.GenerationRequest;
import com.editflowimg.backend.entity.GenerationEntity;
import com.editflowimg.backend.entity.ImageEntity;
import com.editflowimg.backend.entity.UserEntity;
import com.editflowimg.backend.enums.GenerationMode;
import com.editflowimg.backend.repository.GenerationRepository;
import com.editflowimg.backend.repository.ImageRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GenerationServiceTest {

    @Mock
    private GenerationRepository generationRepository;

    @Mock
    private ImageRepository imageRepository;

    @Mock
    private GenerationProcessor generationProcessor;

    @InjectMocks
    private GenerationService generationService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void deveCriarGeracao() {

        TransactionSynchronizationManager.initSynchronization();

        UUID imageId = UUID.randomUUID();

        UserEntity user = new UserEntity();
        user.setId(UUID.randomUUID());

        ImageEntity image = new ImageEntity();
        image.setId(imageId);
        image.setUser(user);

        GenerationRequest request = new GenerationRequest();
        request.setImageId(imageId);
        request.setMode(GenerationMode.PROMPT);
        request.setCustomPrompt("Editar imagem");

        when(imageRepository.findById(imageId))
                .thenReturn(Optional.of(image));

        when(generationRepository.save(any(GenerationEntity.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        GenerationEntity result =
                generationService.createGeneration(request, user);

        assertNotNull(result);

        TransactionSynchronizationManager.clearSynchronization();
    }

    @Test
    void naoDeveCriarSemPrompt() {

        UserEntity user = new UserEntity();
        user.setId(UUID.randomUUID());

        ImageEntity image = new ImageEntity();
        image.setId(UUID.randomUUID());
        image.setUser(user);

        GenerationRequest request = new GenerationRequest();
        request.setImageId(image.getId());
        request.setMode(GenerationMode.PROMPT);

        when(imageRepository.findById(image.getId()))
                .thenReturn(Optional.of(image));

        assertThrows(RuntimeException.class,
                () -> generationService.createGeneration(request, user));

    }
}