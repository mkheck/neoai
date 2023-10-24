package com.thehecklers.neoai;

import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.util.List;

@Node
public record Place(@Id String id,
                    String name,
                    String description,
                    String website,
                    @Relationship(type = "CONTAINS", direction = Relationship.Direction.INCOMING) List<Subcategory> subcategories) {}
