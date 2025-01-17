package com.epam.training.gen.ai.service;

import reactor.core.publisher.Mono;

import java.util.List;

public interface IPromptService {

    List<String> getOpenAIPromptAsyncResponse(String prompt);
}
