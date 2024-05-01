package com.example.experiment.controller;

import org.springframework.ai.openai.OpenAiChatClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class ChatGPTController {

    private final OpenAiChatClient chatClient;

    public ChatGPTController(OpenAiChatClient chatClient) {
        this.chatClient = chatClient;
    }

    // key not working yet
    @GetMapping("/ai/generate")
    public Map<String, String> generate(
            @RequestParam(
                    value = "message",
                    defaultValue = "Tell me a joke"
            ) String message
    ) {
        return Map.of("generation", chatClient.call(message));
    }

}
