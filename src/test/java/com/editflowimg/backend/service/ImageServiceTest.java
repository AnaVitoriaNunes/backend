package com.editflowimg.backend.service;

import com.editflowimg.backend.entity.ImageEntity;
import com.editflowimg.backend.entity.UserEntity;
import com.editflowimg.backend.repository.ImageRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ImageServiceTest {

    @Mock
    private ImageRepository imageRepository;

    @InjectMocks
    private ImageService imageService;

    @BeforeEach
    void setup() throws Exception {
        MockitoAnnotations.openMocks(this);
        imageService = new ImageService(imageRepository);
    }

    @Test
    void deveFazerUploadImagem() throws Exception {

        MockMultipartFile file = new MockMultipartFile(
                "file",
                "foto.png",
                "image/png",
                "imagem".getBytes()
        );

        UserEntity user = new UserEntity();
        user.setId(UUID.randomUUID());

        when(imageRepository.save(any(ImageEntity.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        ImageEntity result = imageService.upload(file, user);

        assertNotNull(result);
        assertEquals("foto.png", result.getOriginalName());
    }

    @Test
    void deveBuscarImagemPorId() {

        UUID id = UUID.randomUUID();

        ImageEntity image = new ImageEntity();
        image.setId(id);

        when(imageRepository.findById(id))
                .thenReturn(Optional.of(image));

        ImageEntity result = imageService.findById(id);

        assertEquals(id, result.getId());
    }

    @Test
    void deveCarregarImagem() throws Exception {

        Path temp = Files.createTempFile("teste", ".png");
        Files.write(temp, "imagem".getBytes());

        ImageEntity image = new ImageEntity();
        image.setStorageKey(temp.toString());

        byte[] result = imageService.loadImage(image);

        assertNotNull(result);
        assertTrue(result.length > 0);

    }
}