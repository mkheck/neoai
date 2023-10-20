package com.thehecklers.neoai;

import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingClient;
import org.springframework.ai.vectorstore.VectorStore;

import java.lang.reflect.Field;
import java.util.*;

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
//        iterator.forEachRemaining(p -> documents.add(new Document(null == p.name() ? "No name" : p.name(),
//                Map.of("query", query))));
        iterator.forEachRemaining(p -> documents.add(new Document(p.toString())));

//        System.out.println(" --- Documents --- ");
//        documents.forEach(System.out::println);
//        System.out.println(" --- Documents --- ");
        return documents;
    }

    public List<Document> similaritySearch(String type, String location) {
        List<Document> documents = new ArrayList<>();

        var places = repo.findPlaces(type, location);
        System.out.println(" --- Step 1: Places --- ");
        places.forEach(System.out::println);

        // First class kludge
        Iterator<Place> iterator = places.iterator();
//        iterator.forEachRemaining(p -> documents.add(new Document(null == p.name() ? "No name" : p.name(),
//                Map.of("type", type, "location", location))));
//        iterator.forEachRemaining(p -> documents.add(new Document(p.toString())));
        iterator.forEachRemaining(p -> documents.add(new Document(p.toString())));

        System.out.println(" --- Step 2: Raw Documents --- ");
        documents.forEach(System.out::println);

        return documents;
    }

    @Override
    public List<Document> similaritySearch(String query, int k) {
        var documents = similaritySearch(query);
        return documents.subList(0, Math.min(documents.size(), k));
    }

    @Override
    public List<Document> similaritySearch(String query, int k, double threshold) {
        //return null;
        return similaritySearch(query, k, 0D);
    }
}
