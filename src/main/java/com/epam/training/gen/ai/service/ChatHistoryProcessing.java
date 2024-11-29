package com.epam.training.gen.ai.service;

import com.microsoft.semantickernel.Kernel;
import com.microsoft.semantickernel.semanticfunctions.KernelFunction;
import com.microsoft.semantickernel.semanticfunctions.KernelFunctionArguments;
import com.microsoft.semantickernel.services.chatcompletion.ChatHistory;
import com.microsoft.semantickernel.services.chatcompletion.ChatMessageContent;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class ChatHistoryProcessing implements IChatHistoryProcessing {

    private final Kernel kernel;

    @Override
    public String processWithHistory(ChatHistory chatHistory, String prompt) {
        var response = kernel.invokeAsync(getChat())
                .withArguments(getKernelFunctionArguments(prompt, chatHistory))
                .block();

        chatHistory.addUserMessage(prompt);
        chatHistory.addAssistantMessage(response.getResult());
        log.info("AI answer:" + response.getResult());
        return response.getResult();
    }

    @Override
    public List<String> getFullHistory(ChatHistory chatHistory) {
        return chatHistory.getMessages().stream().map(ChatMessageContent::getContent).toList();
    }

    @Override
    public String getLastMessage(ChatHistory chatHistory) {
        return chatHistory.getLastMessage()
                .orElseThrow(() -> new IllegalStateException("Chat history is empty"))
                .getContent();
    }

    @Override
    public List<String> getNumberOfMessages(ChatHistory chatHistory, Long numberOfMessages) {
        return chatHistory.getMessages().stream()
                .limit(numberOfMessages)
                .map(ChatMessageContent::getContent)
                .toList();
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
