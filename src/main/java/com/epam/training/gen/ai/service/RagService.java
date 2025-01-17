package com.epam.training.gen.ai.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
@Slf4j
@RequiredArgsConstructor
public class RagService implements IRagService {

    private static final String QUERY = "\n\nQuery: ";

    private final IEmbeddingService embeddingService;
    private final IPromptService promptService;


    @Override
    public List<String> getPromptResponse(String prompt) throws ExecutionException, InterruptedException {
        List<String> embeddings = embeddingService.searchClosest(prompt);
        var query = embeddings.toString() + QUERY + prompt;
        return promptService.getOpenAIPromptAsyncResponse(query);
    }
}
