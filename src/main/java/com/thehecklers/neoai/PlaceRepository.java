package com.thehecklers.neoai;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;

public interface PlaceRepository extends Neo4jRepository<Place, String> {
    @Query("CALL apoc.spatial.geocodeOnce($location) YIELD location\n" +
            "WITH location.latitude as locLat, location.longitude as locLon\n" +
            "MATCH (p:Place)\n" +
            "WHERE point.distance(point({longitude: p.lon, latitude: p.lat}), point({longitude: locLon, latitude: locLat})) < 2500\n" +
            "WITH p\n" +
            "CALL {\n" +
            "    WITH p\n" +
            "    MATCH (p)-[:PROVIDES]->(a:Amenity)\n" +
            "    WHERE a.name =~ ('(?i).*'+$pet+'.*')\n" +
            "    RETURN collect(p) as airbnb\n" +
            "}\n" +
            "WITH p, airbnb\n" +
            "CALL {\n" +
            "    WITH p\n" +
            "    MATCH (p)<-[:CONTAINS]-(s:Subcategory)<-[:CONTAINS]-(c:Category)\n" +
            "    WHERE s.name =~ ('(?i).*'+$pet+'.*')\n" +
            "    OR c.name =~ ('(?i).*'+$pet+'.*')\n" +
            "    RETURN collect(p) as others\n" +
            "}\n" +
            "WITH airbnb+others as places\n" +
            "UNWIND places as place\n" +
            "RETURN place;")
    Iterable<Place> findPlaces(String pet, String location);

    @Query("MATCH (p:Place)-[r:PROVIDES]->(a:Amenity) WHERE a.name =~ ('(?i).*'+$pet+'.*') RETURN p, collect(r), collect(a);")
    Iterable<Place> findInAmenities(String pet);

    @Query("MATCH (p:Place)<-[r:CONTAINS*1..2]-(c:Category) WHERE c.name =~ ('(?i).*'+$pet+'.*') RETURN p, collect(r), collect(c);")
    Iterable<Place> findInCategory(String pet);
}
