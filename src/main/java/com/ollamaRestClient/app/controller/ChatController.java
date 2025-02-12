package com.ollamaRestClient.app.controller;

import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Flux;

import java.io.IOException;
import java.util.Map;

@RestController
public class ChatController {

    private final OllamaChatModel chatModel;

    @Autowired
    public ChatController(OllamaChatModel chatModel) {
        this.chatModel = chatModel;
    }

    @GetMapping("/ai/generate")
    public Map<String, String> generate(@RequestParam(value = "message", defaultValue = "Tell me a joke") String message) {
        return Map.of("generation", this.chatModel.call(message));
    }

    @GetMapping("/ai/generateStream")
    public Flux<ChatResponse> generateStream(@RequestParam(value = "message", defaultValue = "Tell me a joke") String message) {
        Prompt prompt = new Prompt(new UserMessage(message));
        return this.chatModel.stream(prompt);
    }

    @PostMapping("/ai/processFile")
    public Map<String, String> processFile(@RequestParam("file") MultipartFile file) throws IOException {
        // Read the file content
        String fileContent = new String(file.getBytes());

        // Pass the file content to the Ollama model
        String response = this.chatModel.call(fileContent);

        return Map.of("generation", response);
    }

    @PostMapping("/ai/processFileStream")
    public Flux<ChatResponse> processFileStream(@RequestParam("file") MultipartFile file) throws IOException {
        // Read the file content
        String fileContent = new String(file.getBytes());

        // Create a prompt with the file content
        Prompt prompt = new Prompt(new UserMessage(fileContent));

        // Stream the response
        return this.chatModel.stream(prompt);
    }
}