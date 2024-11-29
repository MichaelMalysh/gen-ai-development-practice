package com.epam.training.gen.ai.dto;

import com.microsoft.semantickernel.services.chatcompletion.ChatHistory;
import org.springframework.stereotype.Component;

@Component
public class ChatHistoryInstance {

    private ChatHistory chatHistory;

    public ChatHistory getChatHistory() {
        if (chatHistory == null) {
            chatHistory = new ChatHistory();
        }
        return chatHistory;
    }

    public void deleteChatHistory() {
        chatHistory = null;
    }
}
