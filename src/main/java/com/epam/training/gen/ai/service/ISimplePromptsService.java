package com.epam.training.gen.ai.service;

import com.epam.training.gen.ai.dto.ResponseFormat;

import java.util.List;

public interface ISimplePromptsService {

    List<String> printTopTenBooksOpenAi(String prompt);
    ResponseFormat printTopTenBooksSemanticKernel(String prompt);
}
