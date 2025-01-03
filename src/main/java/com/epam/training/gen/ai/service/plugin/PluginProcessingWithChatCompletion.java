package com.epam.training.gen.ai.service.plugin;

import com.epam.training.gen.ai.plugin.TimePlugin;
import com.microsoft.semantickernel.Kernel;
import com.microsoft.semantickernel.aiservices.openai.chatcompletion.OpenAIChatCompletion;
import com.microsoft.semantickernel.orchestration.FunctionResult;
import com.microsoft.semantickernel.plugin.KernelPlugin;
import com.microsoft.semantickernel.plugin.KernelPluginFactory;
import com.microsoft.semantickernel.semanticfunctions.KernelFunction;
import com.microsoft.semantickernel.semanticfunctions.KernelFunctionFromPrompt;
import com.microsoft.semantickernel.services.chatcompletion.ChatCompletionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * A service for processing plugins with chat completion.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class PluginProcessingWithChatCompletion implements IPluginProcessingWithChatCompletion {

    private final OpenAIChatCompletion openAIChatCompletion;

    /**
     * Invokes the custom time plugin.
     *
     * @param prompt the prompt
     * @return the list of strings
     */
    @Override
    public List<String> invokeCustomTimePlugin(String prompt) {
        KernelPlugin timePlugin = KernelPluginFactory.createFromObject(new TimePlugin(), "TimePlugin");

        var kernel = Kernel.builder()
                .withAIService(ChatCompletionService.class, openAIChatCompletion)
                .withPlugin(timePlugin)
                .build();

        KernelFunction<String> userPromptWithPluginProcessing = KernelFunctionFromPrompt
                .<String>createFromPrompt(prompt + """
                        {{TimePlugin.time locale="Ukraine"}}
                        """)
                .build();

        FunctionResult<String> timeProcessingResult = userPromptWithPluginProcessing
                .invokeAsync(kernel)
                .block();

        log.info("Time processing result: " + timeProcessingResult.getResult());

        return List.of(timeProcessingResult.getResult());
    }
}
