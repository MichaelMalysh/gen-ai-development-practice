package com.epam.training.gen.ai.controller;

import com.epam.training.gen.ai.service.IRagService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/ai/rag")
@RequiredArgsConstructor
public class RagController {

    private final IRagService ragService;

    @GetMapping("/prompt")
    public List<String> getPromptRagResponse(String prompt) throws ExecutionException, InterruptedException {
        return ragService.getPromptResponse(prompt);
    }
}
