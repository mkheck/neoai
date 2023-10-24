package com.thehecklers.neoai;

import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingClient;
import org.springframework.ai.vectorstore.VectorStore;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

public class NeoVectorStore implements VectorStore {
    private final EmbeddingClient client;
    private final PlaceRepository repo;

    public NeoVectorStore(EmbeddingClient client, PlaceRepository repo) {
        this.client = client;
        this.repo = repo;
    }

    @Override
    public void add(List<Document> documents) {
        //repo.saveAll(documents);
    }

    @Override
    public Optional<Boolean> delete(List<String> idList) {
        return Optional.empty();
    }

    @Override
    public List<Document> similaritySearch(String query) {
        List<Document> documents = new ArrayList<>();

        var categories = repo.findInCategory(query);
        Iterator<Place> iterator = categories.iterator();
        iterator.forEachRemaining(p -> documents.add(new Document(p.toString())));

        return documents;
    }

    public List<Document> similaritySearch(String type, String location) {
        List<Document> documents = new ArrayList<>();

        var places = repo.findPlaces(type, location);

        // First class kludge
        Iterator<Place> iterator = places.iterator();
        iterator.forEachRemaining(p -> documents.add(new Document(p.toString())));

        return documents;
    }

    @Override
    public List<Document> similaritySearch(String query, int k) {
        var documents = similaritySearch(query);
        return documents.subList(0, Math.min(documents.size(), k));
    }

    @Override
    public List<Document> similaritySearch(String query, int k, double threshold) {
        return similaritySearch(query, k, 0D);
    }
}
