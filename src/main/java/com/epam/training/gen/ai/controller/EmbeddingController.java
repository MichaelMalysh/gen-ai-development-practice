package com.epam.training.gen.ai.controller;

import com.azure.ai.openai.models.Embeddings;
import com.epam.training.gen.ai.service.IEmbeddingService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * The controller for working with embeddings.
 */
@RestController
@RequestMapping("/ai/embedding")
@RequiredArgsConstructor
public class EmbeddingController {

    private final IEmbeddingService embeddingService;

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
}
