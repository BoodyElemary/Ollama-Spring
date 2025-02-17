package com.ollamaRestClient.app.controller;

import com.ollamaRestClient.app.util.PdfTextExtractor;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.model.Media;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.util.MimeType;
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
    public Map<String, String> processFile(
            @RequestParam(value = "file", required = false) MultipartFile file,
            @RequestParam(value = "message", defaultValue = "You are an advanced AI document classifier with extensive experience in categorizing various types of documents accurately. You excel in understanding different formats and contexts, ensuring that each document is classified correctly based on its content and structure.\n" +
                    "Your task is to classify a given document and return the classification in a JSON format. \n" +
                    "Here are the details of the document you will classify:  \n" +
                    "\n" +
                    "Please ensure that the classification is comprehensive and includes all relevant categories based on the provided details. The JSON format should include the following fields:  \n" +
                    "\n" +
                    "\"document\": the input document text,  \n" +
                    "\"classification\": the determined category or categories,  \n" +
                    "\"confidence_score\": a percentage indicating the confidence level of the classification.\n" +
                    "\n" +
                    "For example, the JSON output should look like this:  \n" +
                    "{  \n" +
                    "  \"document\": \"__________\",  \n" +
                    "  \"classification\": [\"__________\"],  \n" +
                    "  \"confidence_score\": \"__________\"  \n" +
                    "}  \n" +
                    "\n") String message)
            throws IOException {

        // Step 1: Initialize fileContent as an empty string
        String fileContent = "";

        // Step 2: If a file is provided, extract text from it
        if (file != null && !file.isEmpty()) {
            fileContent = PdfTextExtractor.extractTextFromPdf(file);
        }

        // Step 3: Create a UserMessage with the user's message and (optionally) the file content
        String fullMessage = message;
        if (!fileContent.isEmpty()) {
            fullMessage += "\n\nFile Content:\n" + fileContent;
        }
        UserMessage newUserMessage = new UserMessage(fullMessage);

        // Step 4: Create a prompt with the UserMessage
        Prompt prompt = new Prompt(newUserMessage);

        // Step 5: Generate a single response
        ChatResponse chatResponse = this.chatModel.call(prompt);

        // Step 6: Extract the response content
        String response = chatResponse.getResult().getOutput().getContent();

        // Step 7: Return the response
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