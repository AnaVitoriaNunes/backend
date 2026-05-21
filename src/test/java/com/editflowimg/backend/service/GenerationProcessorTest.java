package com.editflowimg.backend.service;

import com.editflowimg.backend.dto.generation.AiResult;
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

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GenerationProcessorTest {

    @Mock
    private GenerationRepository generationRepository;

    @Mock
    private ImageRepository imageRepository;

    @Mock
    private AiService aiService;

    @InjectMocks
    private GenerationProcessor generationProcessor;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void deveProcessarGeracao() throws Exception {

        UUID generationId = UUID.randomUUID();

        Path tempFile = Files.createTempFile("imagem", ".png");
        Files.write(tempFile, "teste".getBytes());

        UserEntity user = new UserEntity();
        user.setId(UUID.randomUUID());

        ImageEntity sourceImage = new ImageEntity();
        sourceImage.setStorageKey(tempFile.toString());

        GenerationEntity generation = new GenerationEntity();
        generation.setId(generationId);
        generation.setUser(user);
        generation.setSourceImage(sourceImage);
        generation.setMode(GenerationMode.PROMPT);
        generation.setCustomPrompt("Editar");

        when(generationRepository.findById(generationId))
                .thenReturn(Optional.of(generation));

        when(aiService.editImage(any(File.class), anyString()))
                .thenReturn("imagem-editada".getBytes());

        when(aiService.generateAnalysis(anyString()))
                .thenReturn(new AiResult(
                        java.util.List.of("analise"),
                        java.util.List.of("mudanca"),
                        "descricao"
                ));

        when(imageRepository.save(any(ImageEntity.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        generationProcessor.process(generationId);

        verify(generationRepository, atLeastOnce()).save(any());

    }
}