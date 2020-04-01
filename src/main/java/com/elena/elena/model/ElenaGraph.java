package com.elena.elena.model;

import lombok.Getter;

import java.util.Map;

public abstract class ElenaGraph {

    @Getter protected ElenaNode currentNode;
    @Getter protected Map<String, ElenaNode> nodes;
    @Getter protected Map<String, ElenaEdge> edges;

    public ElenaNode getNode(String id){
        return this.nodes.get(id);
    }

    public ElenaEdge getEdge(String id){
        return this.edges.get(id);
    }
}
