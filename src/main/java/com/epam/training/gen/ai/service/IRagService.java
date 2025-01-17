package com.epam.training.gen.ai.service;

import java.util.List;
import java.util.concurrent.ExecutionException;

public interface IRagService {

    List<String> getPromptResponse(String prompt) throws ExecutionException, InterruptedException;
}
