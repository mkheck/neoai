package com.thehecklers.neoai;

import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;

@Node
public record Subcategory(@Id String name) {}
//public record Subcategory(@Id String name,
//                       @Relationship(type = "CONTAINS", direction = Relationship.Direction.OUTGOING) List<Place> places) {}
