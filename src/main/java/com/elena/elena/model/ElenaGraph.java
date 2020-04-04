package com.elena.elena.model;

import com.elena.elena.util.ElenaUtils;
import lombok.NonNull;
import org.apache.tinkerpop.gremlin.structure.Edge;
import org.apache.tinkerpop.gremlin.structure.Graph;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.apache.tinkerpop.gremlin.structure.io.IoCore;
import org.apache.tinkerpop.gremlin.tinkergraph.structure.TinkerGraph;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;

public class ElenaGraph<T1, T2, E> extends AbstractElenaGraph<T1, T2, E>{


    public ElenaGraph(@NonNull String graphmlFileName) throws IOException {
        Graph graph = TinkerGraph.open();
        graph.io(IoCore.graphml()).readGraph(ElenaUtils.getFilePath(graphmlFileName));
        this.nodes = new HashMap<>();
        this.edges = new HashMap<>();
        this.importNodes(graph);
        this.importEdges(graph);
    }

    private void importNodes(@NonNull Graph graph){

        Iterator<Vertex> vertices = graph.vertices();
        while(vertices.hasNext()){
            AbstractElenaNode<T1, T2, E> elenaNode = new ElenaNode<>(this, vertices.next());
            this.nodes.put(elenaNode.getId(), elenaNode);
        }
    }

    private void importEdges(@NonNull Graph graph){

        Iterator<Edge> tinkerEdges = graph.edges();
        while(tinkerEdges.hasNext()){
            AbstractElenaEdge<T1, T2, E> elenaEdge = new ElenaEdge<>(this, tinkerEdges.next());
            edges.put(elenaEdge.getId(), elenaEdge);
        }
    }


    @Override
    public AbstractElenaNode<T1, T2, E> getNode(T1 id) {
        return this.nodes.get(id);
    }

    @Override
    public AbstractElenaEdge<T1, T2, E> getEdge(T2 id) {
        return this.edges.get(id);
    }
}
