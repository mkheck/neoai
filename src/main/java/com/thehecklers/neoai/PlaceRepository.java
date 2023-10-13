package com.thehecklers.neoai;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;

public interface PlaceRepository extends Neo4jRepository<Place, String> {
//    public Iterable<Place> findPlacesByAmenitiesLikeIgnoreCaseOrNameLikeIgnoreCaseOrHostLikeIgnoreCase(String search);
//    public Iterable<Place> findPlacesByNameContainingIgnoreCase(String search);
//    public Iterable<Place> findAllByAmenitiesContainsIgnoreCase(String search);
    @Query("MATCH (p:Place)-[r:PROVIDES]->(a:Amenity) WHERE a.name =~ ('(?i).*'+$input+'.*') RETURN p, collect(r), collect(a);")
    public Iterable<Place> findInAmenities(String input);
}
