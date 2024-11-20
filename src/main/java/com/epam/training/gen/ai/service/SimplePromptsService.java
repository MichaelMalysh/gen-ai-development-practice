package com.epam.training.gen.ai.service;

import com.azure.ai.openai.OpenAIAsyncClient;
import com.azure.ai.openai.models.ChatCompletionsOptions;
import com.azure.ai.openai.models.ChatRequestUserMessage;
import com.epam.training.gen.ai.dto.ResponseFormat;
import com.microsoft.semantickernel.Kernel;
import com.microsoft.semantickernel.orchestration.InvocationContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
@Slf4j
public class SimplePromptsService implements ISimplePromptsService {

    @Autowired
    private final OpenAIAsyncClient aiAsyncClient;
    @Autowired
    private final Kernel kernel;
    @Autowired
    private final InvocationContext invocationContext;


    @Value("${client-openai-deployment-name}")
    private String deploymentOrModelName;


    @Override
    public List<String> printTopTenBooksOpenAi(String prompt) {
        return aiAsyncClient
                .getChatCompletions(
                        deploymentOrModelName,
                        new ChatCompletionsOptions(
                                List.of(new ChatRequestUserMessage(prompt))))
                .block()
                .getChoices().stream()
                .map(c -> c.getMessage().getContent())
                .peek(log::info)
                .toList();
    }

    @Override
    public ResponseFormat printTopTenBooksSemanticKernel(String prompt) {
        CompletableFuture<ResponseFormat> futureResponse = new CompletableFuture<>();

        kernel.invokePromptAsync(prompt)
                .withPromptExecutionSettings(invocationContext.getPromptExecutionSettings())
                .subscribe(result -> {
                    String output = Objects.requireNonNull(result.getResult()).toString();
                    log.info("Raw Result: {}", output);

                    List<String> books = parseBooks(output);

                    ResponseFormat responseFormat = ResponseFormat.builder()
                            .input(prompt)
                            .books(books)
                            .build();

                    futureResponse.complete(responseFormat);
                });

        return futureResponse.join();
    }

    private List<String> parseBooks(String output) {
        List<String> bookLines = new ArrayList<>();
        String[] lines = output.split("\n");

        for (String line : lines) {
            line = line.trim();
            if (line.matches("^\\d+\\. .*")) {
                String bookTitle = line.substring(line.indexOf(' ') + 1).replaceAll("^\"|\"$", "").trim();
                bookLines.add(bookTitle);
            }
        }

        return bookLines;
    }
}
