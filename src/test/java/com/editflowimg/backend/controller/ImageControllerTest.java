package com.editflowimg.backend.controller;

import com.editflowimg.backend.entity.ImageEntity;
import com.editflowimg.backend.entity.UserEntity;
import com.editflowimg.backend.exception.AppException;
import com.editflowimg.backend.service.ImageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ImageControllerTest {

    @Mock
    private ImageService imageService;

    @InjectMocks
    private ImageController imageController;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void deveFazerUpload() throws IOException {
        UserEntity user = new UserEntity();
        user.setId(UUID.randomUUID());

        MultipartFile file = new MockMultipartFile(
                "file",
                "imagem.png",
                "image/png",
                "teste".getBytes()
        );

        ImageEntity image = new ImageEntity();
        image.setId(UUID.randomUUID());

        when(imageService.upload(file, user))
                .thenReturn(image);

        ImageEntity result = imageController.upload(file, user);

        assertNotNull(result);
        verify(imageService).upload(file, user);
    }

    @Test
    void deveFazerDownload() throws IOException {
        UUID userId = UUID.randomUUID();

        UserEntity user = new UserEntity();
        user.setId(userId);

        ImageEntity image = new ImageEntity();
        image.setId(UUID.randomUUID());
        image.setOriginalName("foto.png");
        image.setContentType("image/png");
        image.setUser(user);

        byte[] bytes = "imagem".getBytes();

        when(imageService.findById(image.getId()))
                .thenReturn(image);

        when(imageService.loadImage(image))
                .thenReturn(bytes);

        ResponseEntity<byte[]> response =
                imageController.download(image.getId(), user);

        assertEquals(200, response.getStatusCode().value());
        assertArrayEquals(bytes, response.getBody());

        verify(imageService).findById(image.getId());
        verify(imageService).loadImage(image);
    }

    @Test
    void deveLancarErroAoBaixarImagemDeOutroUsuario() {
        UserEntity dono = new UserEntity();
        dono.setId(UUID.randomUUID());

        UserEntity outroUsuario = new UserEntity();
        outroUsuario.setId(UUID.randomUUID());

        ImageEntity image = new ImageEntity();
        image.setId(UUID.randomUUID());
        image.setUser(dono);

        when(imageService.findById(image.getId()))
                .thenReturn(image);

        assertThrows(AppException.class,
                () -> imageController.download(image.getId(), outroUsuario));

        verify(imageService).findById(image.getId());

    }
}