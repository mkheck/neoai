package com.thehecklers.neoai;

import org.springframework.ai.client.AiClient;
import org.springframework.ai.client.Generation;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingClient;
import org.springframework.ai.prompt.Prompt;
import org.springframework.ai.prompt.SystemPromptTemplate;
import org.springframework.ai.prompt.messages.UserMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    //public Generation retrieve(String message) {
    public Generation retrieve(HashMap<String, Object> prompts) {
        // Step 1: Load JSON document as Documents
//        JsonLoader jsonLoader = new JsonLoader(winResource, "name", "year");
//        var documents = jsonLoader.load();

        // Step 2: Create embeddings and save to vector store
        //var vectorStore = new InMemoryVectorStore(embeddingClient);
        var vectorStore = new NeoVectorStore(embeddingClient, repo);
//        vectorStore.add(documents);

        System.out.println("----- Prompts -----" + prompts);
        prompts.forEach((k, v) -> System.out.println("Key: " + k + ", Value: " + v));

        // Step 3: Retrieve documents (hopefully) related to query
        //List<Document> similarDocuments = vectorStore.similaritySearch(message, 100);
        var petString = prompts.getOrDefault("pet", "dog").toString();
        List<Document> similarDocuments = vectorStore.similaritySearch(petString, 100);
        System.out.println("----- Similar Documents (count) -----> " + similarDocuments.size());

        // Step 4: Embed documents into SystemMessage with the `system.st` prompt template
        //var systemMessage = getSystemMessage(similarDocuments, prompts);
        //var systemMessage = systemPromptTemplate.createMessage(Map.of("documents", documents));
        var systemMessage = new SystemPromptTemplate(sysPrompt).createMessage(prompts);
        System.out.println("----- System Message -----> " + systemMessage.getContent());

        UserMessage userMessage = new UserMessage("");

        // Step 5: Ask the AI model
        var prompt = new Prompt(List.of(systemMessage, userMessage));
        System.out.println("----- Prompt -----");
        System.out.println(prompt.getContents());
        System.out.println("----- Prompt -----");
        var response = aiClient.generate(prompt);
        return response.getGeneration();
    }
}
