package com.epam.training.gen.ai.service;

import com.epam.training.gen.ai.dto.ResponseFormat;

import java.util.List;

/**
 * Service interface for generating responses to prompts.
 */
public interface ISimplePromptsService {

    /**
     * Generates a list of books based on the given prompt using OpenAI.
     *
     * @param prompt the prompt to generate books from
     * @return a list of books generated from the prompt
     */
    List<String> printTopTenBooksOpenAi(String prompt);

    /**
     * Generates a list of books based on the given prompt using Semantic Kernel.
     *
     * @param prompt the prompt to generate books from
     * @return a response format containing the input prompt and the generated books
     */
    ResponseFormat printTopTenBooksSemanticKernel(String prompt);
}
