package com.epam.training.gen.ai.controller;

import com.epam.training.gen.ai.dto.ChatHistoryInstance;
import com.epam.training.gen.ai.dto.ResponseFormat;
import com.epam.training.gen.ai.service.IChatHistoryProcessing;
import com.epam.training.gen.ai.service.ISimplePromptsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * This controller provides endpoints for generating responses to prompts
 * using OpenAI and Semantic Kernel services.
 *
 */
@RestController
@RequestMapping("/ai/simple-prompt")
public class SimplePromptController {

    @Autowired
    private ISimplePromptsService simplePromptsService;

    @Autowired
    private IChatHistoryProcessing chatHistoryProcessing;

    @Autowired
    private ChatHistoryInstance chatHistoryInstance;

    /**
     * Generates a list of books based on the given prompt using OpenAI.
     *
     * @param prompt the prompt to generate books from
     * @return a map containing the input prompt and the generated books
     */
    @GetMapping("/top-ten-books")
    public Map<String, List<String>> topTenBooks(@RequestParam("prompt") String prompt) {
        List<String> books = simplePromptsService.printTopTenBooksOpenAi(prompt);
        return Map.of("input", Collections.singletonList(prompt), "books", books);
    }

    /**
     * Generates a list of books based on the given prompt using Semantic Kernel.
     *
     * @param prompt the prompt to generate books from
     * @return a response format containing the input prompt and the generated books
     */
    @GetMapping("/top-ten-books-sa")
    public ResponseFormat topTenBooksSa(@RequestParam("prompt") String prompt) {
        return simplePromptsService.printTopTenBooksSemanticKernel(prompt);
    }

    /**
     * Generates a response to the given prompt using OpenAI.
     *
     * @param prompt the prompt to generate a response from
     * @return a map containing the input prompt and the generated response
     */
    @GetMapping("/chat-with-history")
    public ResponseFormat chatWithHistory(@RequestParam("prompt") String prompt) {
        var chatHistory = chatHistoryInstance.getChatHistory();
        String response = chatHistoryProcessing.processWithHistory(chatHistory, prompt);
        return new ResponseFormat(prompt, List.of(response));
    }

    /**
     * Retrieves the last message from the chat history.
     *
     * @return a map containing the last message from the chat history
     */
    @GetMapping("/chat-history-last-message")
    public ResponseFormat chatHistoryLastMessage() {
        var chatHistory = chatHistoryInstance.getChatHistory();
        String response = chatHistoryProcessing.getLastMessage(chatHistory);
        return new ResponseFormat("Last message", List.of(response));
    }

    /**
     * Retrieves a specified list of messages from the chat history as a response
     */
    @GetMapping("/chat-history-full")
    public ResponseFormat chatHistoryFull() {
        var chatHistory = chatHistoryInstance.getChatHistory();
        List<String> response = chatHistoryProcessing.getFullHistory(chatHistory);
        return new ResponseFormat("Full chat history", response);
    }
}
