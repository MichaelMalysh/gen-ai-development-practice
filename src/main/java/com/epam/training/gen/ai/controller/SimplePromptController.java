package com.epam.training.gen.ai.controller;

import com.epam.training.gen.ai.dto.ResponseFormat;
import com.epam.training.gen.ai.service.ISimplePromptsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/ai/simple-prompt")
public class SimplePromptController {

    @Autowired
    private ISimplePromptsService simplePromptsService;

    @GetMapping("/top-ten-books")
    public Map<String, List<String>> topTenBooks(@RequestParam("prompt") String prompt) {
        List<String> books = simplePromptsService.printTopTenBooksOpenAi(prompt);
        return Map.of("input", Collections.singletonList(prompt), "books", books);
    }

    @GetMapping("/top-ten-books-sa")
    public ResponseFormat topTenBooksSa(@RequestParam("prompt") String prompt) {
        return simplePromptsService.printTopTenBooksSemanticKernel(prompt);
    }
}
