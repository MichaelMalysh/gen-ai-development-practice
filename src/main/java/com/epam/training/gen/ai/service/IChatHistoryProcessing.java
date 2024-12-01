package com.epam.training.gen.ai.service;

import com.microsoft.semantickernel.services.chatcompletion.ChatHistory;

import java.util.List;

public interface IChatHistoryProcessing {

    String processWithHistory(ChatHistory chatHistory, String prompt);

    List<String> getFullHistory(ChatHistory chatHistory);

    String getLastMessage(ChatHistory chatHistory);

    List<String> getNumberOfMessages(ChatHistory chatHistory, Long numberOfMessages);

    List<String> openAIChatHistoryManagement(String prompt);
}
