package com.epam.training.gen.ai.service;

import com.azure.ai.openai.OpenAIAsyncClient;
import com.azure.ai.openai.models.ChatCompletionsOptions;
import com.azure.ai.openai.models.ChatRequestUserMessage;
import com.microsoft.semantickernel.Kernel;
import com.microsoft.semantickernel.orchestration.InvocationContext;
import com.microsoft.semantickernel.semanticfunctions.KernelFunction;
import com.microsoft.semantickernel.semanticfunctions.KernelFunctionArguments;
import com.microsoft.semantickernel.services.chatcompletion.ChatHistory;
import com.microsoft.semantickernel.services.chatcompletion.ChatMessageContent;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class ChatHistoryProcessing implements IChatHistoryProcessing {

    private final Kernel kernel;
    private final InvocationContext invocationContext;
    private final OpenAIAsyncClient aiAsyncClient;
    private final List<String> chatHistory = new ArrayList<>();

    @Value("${client-openai-deployment-name}")
    private String deploymentOrModelName;

    /**
     * Processes the user's prompt with the chat history and returns the AI response.
     *
     * @param chatHistory the current chat history
     * @param prompt the user's input
     * @return the AI response
     */
    @Override
    public String processWithHistory(ChatHistory chatHistory, String prompt) {
        var response = kernel.invokeAsync(getChat())
                .withPromptExecutionSettings(invocationContext.getPromptExecutionSettings())
                .withArguments(getKernelFunctionArguments(prompt, chatHistory))
                .block();

        chatHistory.addUserMessage(prompt);
        chatHistory.addAssistantMessage(response.getResult());
        log.info("AI answer:" + response.getResult());
        return response.getResult();
    }

    /**
     * Retrieves the full chat history.
     *
     * @param chatHistory the chat history
     * @return a list of messages in the chat history
     */
    @Override
    public List<String> getFullHistory(ChatHistory chatHistory) {
        return chatHistory.getMessages().stream().map(ChatMessageContent::getContent).toList();
    }

    /**
     * Retrieves the last message in the chat history.
     *
     * @param chatHistory the chat history
     * @return the last message in the chat history
     */
    @Override
    public String getLastMessage(ChatHistory chatHistory) {
        return chatHistory.getLastMessage()
                .orElseThrow(() -> new IllegalStateException("Chat history is empty"))
                .getContent();
    }

    /**
     * Retrieves a specified number of messages from the chat history.
     *
     * @param chatHistory the chat history
     * @param numberOfMessages the number of messages to retrieve
     * @return a list of messages from the chat history
     */
    @Override
    public List<String> getNumberOfMessages(ChatHistory chatHistory, Long numberOfMessages) {
        return chatHistory.getMessages().stream()
                .limit(numberOfMessages)
                .map(ChatMessageContent::getContent)
                .toList();
    }

    /**
     * Manages the chat history using OpenAI and private List with a history of messages.
     * This method do not implemented in the controller to check,
     * but you can see implmentation of similar approach in the topTenBooks controller method for the Get endpoint.
     *
     * @param prompt the user's input
     * @return a list of AI responses
     */
    @Override
    public List<String> openAIChatHistoryManagement(String prompt) {
        chatHistory.add(prompt);

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

    /**
     * Retrieves the chat history for OpenAI client.
     *
     * @return a list of messages in the chat history
     */
    public List<String> getChatHistory() {
        return new ArrayList<>(chatHistory);
    }

    /**
     * Clears the chat history for Open AI Client.
     */
    public void clearChatHistory() {
        chatHistory.clear();
    }

    /**
     * Creates a kernel function for generating a chat response using a predefined prompt template.
     * <p>
     * The template includes the chat history and the user's message as variables.
     *
     * @return a {@link KernelFunction} for handling chat-based AI interactions
     */
    private KernelFunction<String> getChat() {
        return KernelFunction.<String>createFromPrompt("""
                        {{$chatHistory}}
                        <message role="user">{{$request}}</message>""")
                .build();
    }

    /**
     * Creates the kernel function arguments with the user prompt and chat history.
     *
     * @param prompt the user's input
     * @param chatHistory the current chat history
     * @return a {@link KernelFunctionArguments} instance containing the variables for the AI model
     */
    private KernelFunctionArguments getKernelFunctionArguments(String prompt, ChatHistory chatHistory) {
        return KernelFunctionArguments.builder()
                .withVariable("request", prompt)
                .withVariable("chatHistory", chatHistory)
                .build();
    }

}
