package com.thehecklers.neoai;

import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;

@Node
public record Category(@Id String name) {}
