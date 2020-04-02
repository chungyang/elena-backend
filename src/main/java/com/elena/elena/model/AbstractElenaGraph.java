package com.elena.elena.model;

import lombok.Getter;

import java.util.Map;

public abstract class AbstractElenaGraph {

    @Getter protected AbstractElenaNode currentNode;
    @Getter protected Map<String, AbstractElenaNode> nodes;
    @Getter protected Map<String, AbstractElenaEdge> edges;

    public AbstractElenaNode getNode(String id){
        return this.nodes.get(id);
    }

    public AbstractElenaEdge getEdge(String id){
        return this.edges.get(id);
    }
}
