package com.thehecklers.neoai;

import org.springframework.ai.client.AiClient;
import org.springframework.ai.client.Generation;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingClient;
import org.springframework.ai.loader.impl.JsonLoader;
import org.springframework.ai.prompt.Prompt;
import org.springframework.ai.prompt.SystemPromptTemplate;
import org.springframework.ai.prompt.messages.Message;
import org.springframework.ai.prompt.messages.UserMessage;
import org.springframework.ai.retriever.impl.VectorStoreRetriever;
//import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class RagService {
    @Value("classpath:/data/winners.json")
    private Resource winResource;

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

    public Generation retrieve(String message) {

        // Step 1: Load JSON document as Documents
//        JsonLoader jsonLoader = new JsonLoader(winResource, "name", "year");
//        var documents = jsonLoader.load();

        // Step 2: Create embeddings and save to vector store
        //var vectorStore = new InMemoryVectorStore(embeddingClient);
        //var vectorStore = new VectorStore(embeddingClient) {};
        var vectorStore = new NeoVectorStore(embeddingClient, repo);
//        vectorStore.add(documents);

        // Step 3: Retrieve documents (hopefully) related to query
        var vectorStoreRetriever = new VectorStoreRetriever(vectorStore);
        List<Document> similarDocuments = vectorStoreRetriever.retrieve(message);

        // Step 4: Embed documents into SystemMessage with the `system.st` prompt template
        var systemMessage = getSystemMessage(similarDocuments);
        UserMessage userMessage = new UserMessage(message);

        // Step 5: Ask the AI model
        var prompt = new Prompt(List.of(systemMessage, userMessage));
        var response = aiClient.generate(prompt);
        return response.getGeneration();
    }

    private Message getSystemMessage(List<Document> similarDocuments) {
        var documents = similarDocuments.stream().map(Document::getContent).collect(Collectors.joining("\n"));
        var systemPromptTemplate = new SystemPromptTemplate(sysPrompt);
        var systemMessage = systemPromptTemplate.createMessage(Map.of("documents", documents));
        return systemMessage;
    }
}
