package com.epam.training.gen.ai.controller;

import com.epam.training.gen.ai.dto.OpenAIChatRequest;
import com.epam.training.gen.ai.service.IModelProcessing;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller for working with models.
 */
@RestController
@RequestMapping("/ai/models")
public class WorkingWithModelsController {

    @Autowired
    private IModelProcessing modelProcessing;

    /**
     * Get all available models in EPAM API.
     *
     * @return list of models
     */
    @GetMapping("")
    public List<String> getModels() {
        return modelProcessing.getModels();
    }

    /**
     * Process user request with input params of AI model configuration.
     *
     * @param openAIChatRequest request with parameters
     * @return response from the model
     */
    @PostMapping("")
    public String getUserModel(@Valid @RequestBody OpenAIChatRequest openAIChatRequest) {
        return modelProcessing.getTextResponse(openAIChatRequest);
    }
}
