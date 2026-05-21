package com.editflowimg.backend.service;

import com.editflowimg.backend.dto.generation.AiResult;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AiServiceTest {

    @Test
    void deveGerarAnalise() {

        AiService aiService =
                new AiService("fake-key", "gpt-image-1");

        AiResult result =
                aiService.generateAnalysis("Editar fundo da imagem");

        assertNotNull(result);
        assertEquals(3, result.getAnalysisItems().size());
        assertEquals(3, result.getChangeItems().size());
        assertNotNull(result.getDescriptionText());
    }

}