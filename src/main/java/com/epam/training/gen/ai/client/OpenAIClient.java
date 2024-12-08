package com.epam.training.gen.ai.client;

import com.epam.training.gen.ai.dto.OpenAiModelList;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

/**
 * OpenAI client that handle simple request of processing available models in the EPAM API.
 */
@FeignClient(name = "openAiClient", url = "${client-openai-endpoint}")
public interface OpenAIClient {
    @GetMapping("/openai/deployments")
    OpenAiModelList getModels(@RequestHeader("Api-Key") String apiKey);
}
