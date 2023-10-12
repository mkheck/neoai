package com.thehecklers.neoai;

import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.util.List;

@Node
public record Place(@Id String id,
                    String name,
                    @Relationship(type = "CONTAINS", direction = Relationship.Direction.INCOMING) Subcategory subcategory,
                    @Relationship(type = "HOSTED_BY", direction = Relationship.Direction.OUTGOING) Host host,
                    @Relationship(type = "PROVIDES", direction = Relationship.Direction.OUTGOING) List<Amenity> amenities) {}
