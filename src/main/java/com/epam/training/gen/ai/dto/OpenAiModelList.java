package com.epam.training.gen.ai.dto;

import lombok.Data;

import java.util.List;

/**
 * Represents a list of available models in the OpenAI API.
 */
@Data
public class OpenAiModelList {

    private List<OpenAiModel> data;

    public boolean isEmpty() {
        return data.isEmpty();
    }

    /**
     * Represents a model in the OpenAI API.
     */
    @Data
    public static class OpenAiModel{
        private String model;
    }
}
