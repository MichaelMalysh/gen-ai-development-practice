package com.epam.training.gen.ai.dto;

import com.microsoft.semantickernel.services.chatcompletion.ChatHistory;
import org.springframework.stereotype.Component;

/**
 * Singleton class for storing the chat history.
 */
@Component
public class ChatHistoryInstance {

    private ChatHistory chatHistory;

    /**
     * Retrieves the chat history instance.
     *
     * @return the chat history
     */
    public ChatHistory getChatHistory() {
        if (chatHistory == null) {
            chatHistory = new ChatHistory();
        }
        return chatHistory;
    }

    /**
     * Deletes the chat history.
     */
    public void deleteChatHistory() {
        chatHistory = null;
    }
}
