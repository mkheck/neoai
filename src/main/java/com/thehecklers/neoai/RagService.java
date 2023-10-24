package com.thehecklers.neoai;

import org.springframework.ai.client.AiClient;
import org.springframework.ai.client.Generation;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingClient;
import org.springframework.ai.prompt.Prompt;
import org.springframework.ai.prompt.SystemPromptTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RagService {
    @Value("classpath:/prompts/system.st")
    private Resource sysPrompt;

    //    private final AzureOpenAiClient aiClient;
    private final AiClient aiClient;
    private final EmbeddingClient embeddingClient;

    private final PlaceRepository repo;

    public RagService(AiClient aiClient, EmbeddingClient embeddingClient, PlaceRepository repo) {
        this.aiClient = aiClient;
        this.embeddingClient = embeddingClient;
        this.repo = repo;
    }

/*
    public Generation getIt(HashMap<String, Object> prompts) {
        prompts.put("documents", "");
        var systemMessage = new SystemPromptTemplate(sysPrompt).createMessage(prompts);

        // Step 5: Ask the AI model
        var prompt = new Prompt(List.of(systemMessage));
        var response = aiClient.generate(prompt);
        return response.getGeneration();
    }
*/

    public Generation retrieve(HashMap<String, Object> prompts) {
        // Create embeddings and save to vector store
        var vectorStore = new NeoVectorStore(embeddingClient, repo);

        // Retrieve documents (hopefully) related to query
        var typeString = prompts.getOrDefault("type", "hotel").toString();
        var locString = prompts.getOrDefault("location", "New York").toString();
        List<Document> similarDocuments = vectorStore.similaritySearch(typeString, locString);

        // Embed documents into SystemMessage with the `system.st` prompt template
        String documents = similarDocuments.stream().map(entry -> entry.getContent()).collect(Collectors.joining("\n"));
        prompts.put("documents", documents);
        var systemMessage = new SystemPromptTemplate(sysPrompt).createMessage(prompts);

        // Query the AI model
        var prompt = new Prompt(List.of(systemMessage));
        var response = aiClient.generate(prompt);
        return response.getGeneration();
    }
}
