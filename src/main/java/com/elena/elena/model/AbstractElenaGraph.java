package com.elena.elena.model;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.util.Map;
import java.util.Optional;

public abstract class AbstractElenaGraph{

    @Setter @Getter protected AbstractElenaNode currentNode;

    public abstract Optional<AbstractElenaNode> getNode(@NonNull String id);

    public abstract AbstractElenaEdge getEdge(@NonNull String id);

    public abstract void cleanup();

}
