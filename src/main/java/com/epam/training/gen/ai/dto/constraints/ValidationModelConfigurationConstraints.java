package com.epam.training.gen.ai.dto.constraints;

/**
 * Validation constraints for model configuration.
 */
public class ValidationModelConfigurationConstraints {

    public static final int MAX_TOKENS_DEFAULT_VALUE = 100;
    public static final int MAX_TOKENS_MIN_VALUE = 1;
    public static final int MAX_TOKENS_MAX_VALUE = 1000;

    public static final double TEMPERATURE_DEFAULT_VALUE = 1.0;
    public static final int TEMPERATURE_MIN_VALUE = 0;
    public static final int TEMPERATURE_MAX_VALUE = 1;
    public static final int DEFAULT_RESULTS_PER_PROMPT = 1;

    public static final String DEPLOYMENT_NAME_DEFAULT_VALUE = "gpt-35-turbo";

    public static final String PROMPT_NOT_NULL = "Prompt cannot be null";
    public static final String MAX_TOKENS_MIN_VALUE_ERROR = "maxTokens must be at least 1";
    public static final String MAX_TOKENS_MAX_VALUE_ERROR = "maxTokens must be less than or equal to 1000";
    public static final String TEMPERATURE_MIN_VALUE_ERROR = "temperature must be at least 0.0";
    public static final String TEMPERATURE_MAX_VALUE_ERROR = "temperature must be less than or equal to 1.0";
}
