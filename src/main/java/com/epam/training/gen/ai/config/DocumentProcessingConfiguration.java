package com.epam.training.gen.ai.config;

import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
public class DocumentProcessingConfiguration {

    @Bean
    PDFTextStripper pdfTextStripper() throws IOException {
        return new PDFTextStripper();
    }
}
