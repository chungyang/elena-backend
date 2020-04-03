package com.elena.elena.model;

import org.apache.tinkerpop.gremlin.structure.Edge;

import java.util.Map;

public class ElenaEdge<T, E> extends AbstractElenaEdge<T, E> {

    private final Edge tinkerEdge;

    private final String LENGTH__PROPERTY_KEY = "length";

    public  ElenaEdge(Edge tinkerEdge){
        this.tinkerEdge = tinkerEdge;

    }

    @Override
    public T getId() {
        return null;
    }

    @Override
    public float getEdgeDistance() {
        return 0;
    }

    @Override
    public float getEdgeElevation() {
        return 0;
    }

    @Override
    public AbstractElenaNode<T> getOriginNode() {
        return null;
    }

    @Override
    public AbstractElenaNode<T> getDestinationNode() {
        return null;
    }

    @Override
    public Map<String, E> getProperties() {
        return null;
    }
}
