package com.epam.training.gen.ai.controller;

import com.epam.training.gen.ai.service.plugin.IPluginProcessingWithChatCompletion;
import com.epam.training.gen.ai.service.plugin.SmartHousePluginProcessing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * The controller for working with plugins.
 */
@RestController
@RequestMapping("/ai/plugins")
public class WorkWithPluginsController {

    @Autowired
    private IPluginProcessingWithChatCompletion pluginProcessingWithChatCompletion;

    @Autowired
    private SmartHousePluginProcessing deviceService;

    /**
     * Invokes the custom time plugin.
     *
     * @param prompt the prompt
     * @return the list of strings
     */
    @GetMapping("/time")
    public List<String> invokeCustomTimePlugin(@RequestParam("prompt") String prompt) {
        return pluginProcessingWithChatCompletion.invokeCustomTimePlugin(prompt);
    }

    /**
     * Turns on/off the device.
     *
     * @param deviceName the device name
     * @param operation  the operation
     * @return the string
     */
    @PostMapping("/manage/{deviceName}")
    public String turnOn(@PathVariable String deviceName,
                         @RequestParam("operation") String operation) {
        return deviceService.doOperation(operation, deviceName);
    }

}
