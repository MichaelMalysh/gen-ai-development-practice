package com.epam.training.gen.ai.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

import static com.epam.training.gen.ai.dto.constraints.ValidationModelConfigurationConstraints.*;

/**
 * Represents a request to the OpenAI chat API.
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
public class OpenAIChatRequest {

    @NotNull(message = PROMPT_NOT_NULL)
    @JsonProperty("prompt")
    private String prompt;

    @Min(value = MAX_TOKENS_MIN_VALUE, message = MAX_TOKENS_MIN_VALUE_ERROR)
    @Max(value = MAX_TOKENS_MAX_VALUE, message = MAX_TOKENS_MAX_VALUE_ERROR)
    @JsonProperty(value = "max_tokens")
    private Integer maxTokens = MAX_TOKENS_DEFAULT_VALUE;

    @Min(value = TEMPERATURE_MIN_VALUE, message = TEMPERATURE_MIN_VALUE_ERROR)
    @Max(value = TEMPERATURE_MAX_VALUE, message = TEMPERATURE_MAX_VALUE_ERROR)
    @JsonProperty(value = "temperature")
    private Double temperature = TEMPERATURE_DEFAULT_VALUE;

    @JsonProperty(value = "model")
    private String deploymentName = DEPLOYMENT_NAME_DEFAULT_VALUE;

    @JsonProperty(value = "stop_words")
    private List<String> stopWords = List.of();

    @JsonProperty(value = "results_per_prompt")
    private Integer resultsPerPrompt = DEFAULT_RESULTS_PER_PROMPT;

}