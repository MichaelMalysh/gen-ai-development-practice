package com.epam.training.gen.ai.service;

import com.azure.ai.openai.OpenAIAsyncClient;
import com.azure.ai.openai.models.ChatCompletions;
import com.azure.ai.openai.models.ChatCompletionsOptions;
import com.azure.ai.openai.models.ChatRequestUserMessage;
import com.microsoft.semantickernel.Kernel;
import com.microsoft.semantickernel.orchestration.FunctionInvocation;
import com.microsoft.semantickernel.orchestration.FunctionResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class PromptService implements IPromptService {

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private OpenAIAsyncClient openAIAsyncClient;

    @Autowired
    private Kernel kernel;

    @Value("${client-openai-deployment-name}")
    private String deploymentOrModelName;


    @Override
    public List<String> getOpenAIPromptAsyncResponse(String prompt) {
        return openAIAsyncClient
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
}
