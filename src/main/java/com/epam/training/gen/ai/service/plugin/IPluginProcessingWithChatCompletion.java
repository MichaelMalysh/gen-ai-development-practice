package com.epam.training.gen.ai.service.plugin;

import java.util.List;

public interface IPluginProcessingWithChatCompletion {

   List<String> invokeCustomTimePlugin(String prompt);
}
