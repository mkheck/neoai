package com.thehecklers.neoai;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;

public interface PlaceRepository extends Neo4jRepository<Place, String> {
    //    public Iterable<Place> findPlacesByAmenitiesLikeIgnoreCaseOrNameLikeIgnoreCaseOrHostLikeIgnoreCase(String search);
//    public Iterable<Place> findPlacesByNameContainingIgnoreCase(String search);
//    public Iterable<Place> findAllByAmenitiesContainsIgnoreCase(String search);
//    @Query("MATCH (p:Place)-[r:PROVIDES]->(a:Amenity) WHERE a.name =~ ('(?i).*'+$input+'.*') RETURN p, collect(r), collect(a);")
    //@Query("MATCH (p:Place)-[r:PROVIDES]->(a:Amenity) WHERE a.name =~ ('(?i).*'+$input+'.*') RETURN p, collect(r), collect(a);")
    @Query("MATCH (p:Place)-[r:PROVIDES]->(a:Amenity) WHERE a.name =~ ('(?i).*'+$pet+'.*') RETURN p, collect(r), collect(a);")
    Iterable<Place> findInAmenities(String pet);

    @Query("MATCH (p:Place)<-[r:CONTAINS*1..2]-(c:Category) WHERE c.name =~ ('(?i).*'+$pet+'.*') RETURN p, collect(r), collect(c);")
    Iterable<Place> findInCategory(String pet);
}
