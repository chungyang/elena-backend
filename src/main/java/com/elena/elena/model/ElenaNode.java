package com.elena.elena.model;

import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.apache.tinkerpop.gremlin.tinkergraph.structure.TinkerVertex;

public class ElenaNode<T> extends AbstractElenaNode<T> {

    private final Vertex tinkerVertex;

    public ElenaNode(Vertex tinkerVertex){
        this.tinkerVertex = tinkerVertex;
    }

}
