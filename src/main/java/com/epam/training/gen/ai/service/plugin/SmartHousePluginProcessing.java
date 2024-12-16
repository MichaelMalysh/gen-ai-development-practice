package com.epam.training.gen.ai.service.plugin;

import com.epam.training.gen.ai.plugin.SmartHousePlugin;
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

/**
 *  A service for processing smart house plugins.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class SmartHousePluginProcessing implements ISmartHousePluginProcessing {

    private final OpenAIChatCompletion openAIChatCompletion;

    /**
     * Do an operation on a smart house device.
     *
     * @param operation the operation to perform
     * @param deviceName the name of the device
     * @return the result of the operation
     */
    @Override
    public String doOperation(String operation, String deviceName) {
        FunctionResult<String> timeProcessingResult = preparePluginProcessing(operation, deviceName);


        return timeProcessingResult.getResult();
    }

    /**
     * Prepares the plugin processing.
     *
     * @param operation the operation to perform
     * @param deviceName the name of the device
     * @return the result of the operation
     */
    private FunctionResult<String> preparePluginProcessing(String operation, String deviceName) {
        KernelPlugin timePlugin = KernelPluginFactory.createFromObject(new SmartHousePlugin(), "SmartHouse");

        var kernel = Kernel.builder()
                .withAIService(ChatCompletionService.class, openAIChatCompletion)
                .withPlugin(timePlugin)
                .build();

        KernelFunction<String> userPromptWithPluginProcessing = KernelFunctionFromPrompt
                .<String>createFromPrompt(
                        String.format("{{SmartHouse.%s deviceName=\"%s\"}}", operation, deviceName)
                        )
                .build();

        return userPromptWithPluginProcessing
                .invokeAsync(kernel)
                .block();
    }
}
