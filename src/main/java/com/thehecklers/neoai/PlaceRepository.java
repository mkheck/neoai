package com.thehecklers.neoai;

import org.springframework.data.neo4j.repository.Neo4jRepository;

public interface PlaceRepository extends Neo4jRepository<Place, String> {
//    public Iterable<Place> findPlacesByAmenitiesLikeIgnoreCaseOrNameLikeIgnoreCaseOrHostLikeIgnoreCase(String search);
    public Iterable<Place> findPlacesByNameContainingIgnoreCase(String search);
}
