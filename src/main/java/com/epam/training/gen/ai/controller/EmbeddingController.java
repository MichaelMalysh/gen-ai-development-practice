package com.epam.training.gen.ai.controller;

import com.azure.ai.openai.models.Embeddings;
import com.epam.training.gen.ai.handler.PdfHandler;
import com.epam.training.gen.ai.service.IEmbeddingService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

/**
 * The controller for working with embeddings.
 */
@RestController
@RequestMapping("/ai/embedding")
@AllArgsConstructor
public class EmbeddingController {

    private final IEmbeddingService embeddingService;
    private final PDFTextStripper pdf;

    /**
     * Builds embeddings from text.
     *
     * @param text the text
     * @return the embeddings
     */
    @GetMapping("/build")
    public Mono<Embeddings> buildEmbeddingsFromText(@RequestParam String text) {
        return embeddingService.retrieveEmbeddings(text);
    }

    /**
     * Builds and stores embeddings from text.
     *
     * @param text the text
     * @return the string
     * @throws ExecutionException
     * @throws InterruptedException
     */
    @PostMapping("/build/store")
    public String buildAndStoreEmbeddingsFromText(@RequestParam String text) throws ExecutionException, InterruptedException {
        try {
            embeddingService.createEmbeddings(text);
            return "Embedding Created Successfully";
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Searches for the closest embeddings.
     * @param text - input parameter of search
     * @return closest element from vector
     * @throws ExecutionException
     * @throws InterruptedException
     */
    @GetMapping("/search")
    public List<String> searchClosestEmbeddings(@RequestParam String text) throws ExecutionException, InterruptedException {
        return embeddingService.searchClosest(text);
    }

    @PostMapping("/create/document")
    public String downloadDocumentIntoVectorContext(@RequestParam String filePath){
        var result = new StringBuilder();
        Optional<String> fileContent = new PdfHandler(filePath, pdf).getContent();
        if (fileContent.isPresent()) {
            try {
                embeddingService.createEmbeddings(fileContent.get());
                result.append("Embedding Created Successfully");
            } catch (Exception e) {
                result.append("Error while creating embeddings");
            }
        }
        result.append("\n").append("Document content: ").append(fileContent.get().length());
        return result.toString();
    }
}
