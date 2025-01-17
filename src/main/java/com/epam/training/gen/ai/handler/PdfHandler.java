package com.epam.training.gen.ai.handler;

import lombok.RequiredArgsConstructor;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

@RequiredArgsConstructor
public class PdfHandler {

    private final String pathToPdf;

    private final PDFTextStripper pdfStripper;

    private PDDocument getDocument() throws IOException {
        return PDDocument.load(new File(pathToPdf));
    }

    public Optional<String> getContent() {
        try (PDDocument document = getDocument()) {
            return Optional.of(pdfStripper.getText(document));
        } catch (IOException e) {
            return Optional.empty();
        }
    }
}
