package com.elena.elena.model;

import com.elena.elena.util.ElenaUtils;
import org.apache.tinkerpop.gremlin.structure.Edge;
import org.apache.tinkerpop.gremlin.structure.Graph;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.apache.tinkerpop.gremlin.structure.io.IoCore;
import org.apache.tinkerpop.gremlin.tinkergraph.structure.TinkerGraph;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ElenaGraph<T1, T2, E> extends AbstractElenaGraph<T1, T2, E>{

    public ElenaGraph(String graphmlFileName) throws IOException {
        Graph graph = TinkerGraph.open();
        graph.io(IoCore.graphml()).readGraph(ElenaUtils.getFilePath(graphmlFileName));
    }

    @Override
    public AbstractElenaNode<T1> getCurrentNode() {
        return null;
    }

    @Override
    public AbstractElenaNode<T1> getNode(T1 id) {
        return null;
    }

    @Override
    public AbstractElenaEdge<T2, E> getEdge(T2 id) {
        return null;
    }
}
