package com.epam.training.gen.ai.service;

import com.epam.training.gen.ai.dto.OpenAIChatRequest;

import java.util.List;

public interface IModelProcessing {

    List<String> getModels();

    String getTextResponse(OpenAIChatRequest openAIChatRequest);
}
