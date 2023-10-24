package com.thehecklers.neoai;

import org.springframework.ai.client.AiClient;
import org.springframework.ai.client.Generation;
import org.springframework.ai.embedding.EmbeddingClient;
import org.springframework.ai.prompt.Prompt;
import org.springframework.ai.prompt.SystemPromptTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.util.HashMap;

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

    public Generation retrieve(HashMap<String, Object> prompts) {
        // Create embeddings and save to vector store
        var vectorStore = new NeoVectorStore(embeddingClient, repo);

        var typeString = prompts.getOrDefault("type", "hotel").toString();
        var locString = prompts.getOrDefault("location", "New York").toString();

        // Embed documents into SystemMessage with the `system.st` prompt template
        prompts.put("documents", vectorStore.similaritySearch(typeString, locString));
        var systemMessage = new SystemPromptTemplate(sysPrompt).createMessage(prompts);

        // Query the AI model
        return aiClient.generate(new Prompt(systemMessage)).getGeneration();
    }
}
