package com.elena.elena.model;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.util.Collection;
import java.util.Optional;

public abstract class AbstractElenaGraph{

    @Setter @Getter protected AbstractElenaNode currentNode;

    public abstract Collection<AbstractElenaNode> getAllNodes();

    public abstract Collection<AbstractElenaEdge> getAllEdges();

    public abstract Collection<String> getLocationNames();

    public abstract Optional<AbstractElenaNode> getNodeByID(@NonNull String id);

    public abstract Optional<AbstractElenaNode> getNodeByAddress(@NonNull String address);

    public abstract Optional<AbstractElenaNode> getNodeByCoordinate(@NonNull String latitude, @NonNull String longitutde);

    public abstract AbstractElenaEdge getEdge(@NonNull String id);

    public abstract void cleanup();

}
