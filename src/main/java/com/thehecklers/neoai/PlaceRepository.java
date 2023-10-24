package com.thehecklers.neoai;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;

public interface PlaceRepository extends Neo4jRepository<Place, String> {
//    @Query("MATCH (p:Place)<-[:CONTAINS]-(s:Subcategory)<-[:CONTAINS]-(c:Category)\n" +
//            "WHERE p.city IN ['Manhattan', 'Brooklyn', 'New York', 'Queens', 'Staten Island', 'The Bronx']\n" +
//            "AND s.name =~ ('(?i).*'+$pet+'.*')\n" +
//            "OR c.name =~ ('(?i).*'+$pet+'.*')\n" +
//            "RETURN p, collect(s), collect(c);")
//    Iterable<Place> findPlaces(String pet);

    @Query("MATCH (p:Place)<-[r:CONTAINS]-(s:Subcategory)<-[r2:CONTAINS]-(c:Category)\n" +
            "WHERE p.city = $location\n" +
            "AND s.name CONTAINS toLower($type)\n" +
            "RETURN p, collect(r), collect(s), collect(r2), collect(c);")
    Iterable<Place> findPlaces(String type, String location);

    @Query("MATCH (p:Place)<-[r:CONTAINS*1..2]-(c:Category) WHERE c.name =~ ('(?i).*'+$pet+'.*') RETURN p, collect(r), collect(c);")
    Iterable<Place> findInCategory(String pet);
}
