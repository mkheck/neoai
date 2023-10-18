package com.thehecklers.neoai;

import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.util.List;

@Node
public record Subcategory(@Id String name,
                       @Relationship(type = "CONTAINS", direction = Relationship.Direction.INCOMING) List<Category> categories) {}
