package com.epam.training.gen.ai.config;

import com.azure.ai.openai.OpenAIAsyncClient;
import com.azure.ai.openai.OpenAIClientBuilder;
import com.azure.core.credential.AzureKeyCredential;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for setting up OpenAI components.
 * <p>
 * This configuration provides a bean necessary for the interaction with
 * Azure OpenAI services.
 */
@Configuration
public class OpenAIConfiguration {

    @Value("${client-openai-key}")
    private String openAIKey;
    @Value("${client-openai-endpoint}")
    private String openAIEndpoint;

    /**
     * Creates an {@link OpenAIAsyncClient} bean to communicate with Azure OpenAI.
     *
     * @return an instance of {@link OpenAIAsyncClient}
     */
    @Bean
    public OpenAIAsyncClient openAIAsyncClient() {
        return new OpenAIClientBuilder()
                .credential(new AzureKeyCredential(openAIKey))
                .endpoint(openAIEndpoint)
                .buildAsyncClient();
    }


}
