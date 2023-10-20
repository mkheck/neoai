package com.thehecklers.neoai;

import org.springframework.ai.client.AiClient;
import org.springframework.ai.client.Generation;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingClient;
import org.springframework.ai.prompt.Prompt;
import org.springframework.ai.prompt.SystemPromptTemplate;
import org.springframework.ai.prompt.messages.Message;
import org.springframework.ai.prompt.messages.SystemMessage;
import org.springframework.ai.prompt.messages.UserMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

    public Generation getIt(HashMap<String, Object> prompts) {
        System.out.println("----- PromptStuffing: Start -----\n");
        prompts.put("documents", "");
        var systemMessage = new SystemPromptTemplate(sysPrompt).createMessage(prompts);

        // Step 5: Ask the AI model
        var prompt = new Prompt(List.of(systemMessage));
        System.out.println("----- NonNeo4j: Send to AI -----");
        System.out.println(prompt.getContents());
        var response = aiClient.generate(prompt);
        System.out.println("----- PromptStuffing: End -----\n");
        return response.getGeneration();
    }

    public Generation retrieve(HashMap<String, Object> prompts) {
        System.out.println("----- RAG: Start -----\n");
        // Step 2: Create embeddings and save to vector store
        var vectorStore = new NeoVectorStore(embeddingClient, repo);

        System.out.println("----- Search Values ----- \n" + prompts + "\n");
//        prompts.forEach((k, v) -> System.out.println("Key: " + k + ", Value: " + v));

        // Step 3: Retrieve documents (hopefully) related to query
        var typeString = prompts.getOrDefault("type", "hotel").toString();
        var locString = prompts.getOrDefault("location", "New York").toString();
        List<Document> similarDocuments = vectorStore.similaritySearch(typeString, locString);
        System.out.println("----- Similar Documents (count) -----> " + similarDocuments.size());
        System.out.println();

        // Step 4: Embed documents into SystemMessage with the `system.st` prompt template
        String documents = similarDocuments.stream().map(entry -> entry.getContent()).collect(Collectors.joining("\n"));
        prompts.put("documents", documents);
        var systemMessage = new SystemPromptTemplate(sysPrompt).createMessage(prompts);

        // Step 5: Ask the AI model
        var prompt = new Prompt(List.of(systemMessage));
        System.out.println("----- Send to AI -----");
        System.out.println(prompt.getContents());
        var response = aiClient.generate(prompt);
        System.out.println("----- RAG: End -----\n");
        return response.getGeneration();
    }
}
