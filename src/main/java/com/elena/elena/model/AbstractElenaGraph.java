package com.elena.elena.model;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.util.Map;

public abstract class AbstractElenaGraph<T1, T2, E> {

    @Setter @Getter protected AbstractElenaNode<T1, T2, E> currentNode;
    protected Map<T1, AbstractElenaNode<T1, T2, E>> nodes;
    protected Map<T2, AbstractElenaEdge<T1, T2, E>> edges;

    public abstract AbstractElenaNode<T1, T2, E> getNode(@NonNull T1 id);

    public abstract AbstractElenaEdge<T1, T2, E> getEdge(@NonNull T2 id);

}
