package com.epam.training.gen.ai.service;

import com.azure.ai.openai.OpenAIAsyncClient;
import com.epam.training.gen.ai.client.OpenAIClient;
import com.epam.training.gen.ai.dto.OpenAIChatRequest;
import com.epam.training.gen.ai.dto.OpenAiModelList;
import com.microsoft.semantickernel.Kernel;
import com.microsoft.semantickernel.aiservices.openai.chatcompletion.OpenAIChatCompletion;
import com.microsoft.semantickernel.exceptions.SKException;
import com.microsoft.semantickernel.orchestration.FunctionResult;
import com.microsoft.semantickernel.orchestration.InvocationContext;
import com.microsoft.semantickernel.orchestration.PromptExecutionSettings;
import com.microsoft.semantickernel.services.chatcompletion.ChatCompletionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Service for processing OpenAI models.
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class ModelProcessingService implements IModelProcessing {

    @Value("${client-openai-key}")
    private String azureClientKey;

    private final OpenAIClient openAIClient;
    private final OpenAIAsyncClient openAIAsyncClient;
    private final InvocationContext invocationContext;

    /**
     * Get all available models in EPAM API.
     *
     * @return list of models
     */
    @Override
    public List<String> getModels() {
        OpenAiModelList models = openAIClient.getModels(azureClientKey);

        if(models.isEmpty()) {
            log.error("No models found or connection is not established");
        }

        return models.getData()
                .stream()
                .map(OpenAiModelList.OpenAiModel::getModel)
                .toList();
    }

    /**
     * Process user request with input parameters of AI model configuration.
     *
     * @param openAIChatRequest request with parameters
     * @return response from the model
     */
    @Override
    public String getTextResponse(OpenAIChatRequest openAIChatRequest) {
        try {
            validateRequest(openAIChatRequest); // Validate parameters

            FunctionResult<Object> response = processPrompt(openAIChatRequest.getPrompt(),
                    buildPromptExecutionSettings(openAIChatRequest),
                    buildKernel(buildChatCompletionService(openAIChatRequest))
            );

            return response.getResult().toString();

        } catch (IllegalArgumentException e) {
            // Return user-friendly error message and log technical error
            log.error("Error in getting text response: {}", e.getMessage());
            return "Invalid input: " + e.getMessage();
        } catch (SKException e) {
            // Handle specific SKException or other known exceptions
            log.error("Semantic Kernel error: {}", e.getMessage());
            return "Error processing your request. Please try again later.";
        } catch (Exception e) {
            // Catch all for unexpected exceptions
            log.error("An unexpected error occurred: {}", e.getMessage());
            return "An unexpected error occurred. Please try again later.";
        }
    }

    /**
     * Process the prompt with the given settings and kernel.
     *
     * @param prompt the prompt to process
     * @param settings the settings for the prompt execution
     * @param kernel the kernel to use for processing
     * @return the result of the prompt processing
     */
    private FunctionResult<Object> processPrompt(String prompt, PromptExecutionSettings settings, Kernel kernel) {
        return kernel.invokePromptAsync(prompt)
                .withPromptExecutionSettings(settings)
                .withInvocationContext(invocationContext)
                .block();
    }

    /**
     * Build the chat completion service for the given request.
     *
     * @param openAIChatRequest the request to build the service for
     * @return the chat completion service
     */
    private ChatCompletionService buildChatCompletionService(OpenAIChatRequest openAIChatRequest) {
        return OpenAIChatCompletion.builder()
                .withModelId(openAIChatRequest.getDeploymentName())
                .withOpenAIAsyncClient(openAIAsyncClient)
                .build();
    }

    /**
     * Build the prompt execution settings for the given request.
     *
     * @param openAIChatRequest the request to build the settings for
     * @return the prompt execution settings
     */
    private PromptExecutionSettings buildPromptExecutionSettings(OpenAIChatRequest openAIChatRequest) {
        return PromptExecutionSettings.builder()
                .withTemperature(openAIChatRequest.getTemperature())
                .withMaxTokens(openAIChatRequest.getMaxTokens())
                .withStopSequences(openAIChatRequest.getStopWords())
                .withResultsPerPrompt(openAIChatRequest.getResultsPerPrompt())
                .build();
    }

    /**
     * Build the kernel for the given chat completion service.
     *
     * @param chatCompletionService the chat completion service to build the kernel for
     * @return the kernel
     */
    private Kernel buildKernel(ChatCompletionService chatCompletionService) {
        return Kernel.builder()
                .withAIService(ChatCompletionService.class, chatCompletionService)
                .build();
    }

    /**
     * Validate the request parameters.
     *
     * @param request the request to validate
     */
    private void validateRequest(OpenAIChatRequest request) {
        Optional.ofNullable(request.getDeploymentName())
                .orElseThrow(() -> new IllegalArgumentException("Deployment name is null"));

        Optional.ofNullable(request.getPrompt())
                .orElseThrow(() -> new IllegalArgumentException("Prompt is null"));

        if (!validateModelName(request.getDeploymentName())) {
            throw new IllegalArgumentException("Model name is not valid");
        }
    }

    /**
     * Validate the model name.
     *
     * @param modelName the model name to validate
     * @return true if the model name is valid, false otherwise
     */
    private boolean validateModelName(String modelName) {
        return getModels().contains(modelName);
    }
}
