package com.thehecklers.neoai;

import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

@Node
public record Place(@Id String id,
                    String name,
                    @Relationship(type = "CONTAINS", direction = Relationship.Direction.INCOMING) Subcategory subcategory) {}
