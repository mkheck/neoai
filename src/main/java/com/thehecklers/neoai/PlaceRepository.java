package com.thehecklers.neoai;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;

public interface PlaceRepository extends Neo4jRepository<Place, String> {
    @Query("MATCH (p:Place)-[r:PROVIDES]->(a:Amenity) WHERE a.name =~ ('(?i).*'+$pet+'.*') RETURN p, collect(r), collect(a);")
    Iterable<Place> findInAmenities(String pet);

    @Query("MATCH (p:Place)<-[r:CONTAINS*1..2]-(c:Category) WHERE c.name =~ ('(?i).*'+$pet+'.*') RETURN p, collect(r), collect(c);")
    Iterable<Place> findInCategory(String pet);
}
