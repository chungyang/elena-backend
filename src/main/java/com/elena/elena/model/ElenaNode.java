package com.elena.elena.model;

import org.apache.tinkerpop.gremlin.structure.Direction;
import org.apache.tinkerpop.gremlin.structure.Vertex;

import java.util.ArrayList;
import java.util.List;

public class ElenaNode<T1, T2, E> extends AbstractElenaNode<T1, T2, E> {

    private final Vertex tinkerVertex;
    private final AbstractElenaGraph<T1, T2, E> graph;
    private float distanceWeight;
    private float elevationWeight;

    public ElenaNode(AbstractElenaGraph graph, Vertex tinkerVertex){

        this.graph = graph;
        this.tinkerVertex = tinkerVertex;
    }

    @Override
    public T1 getId() {
        return (T1) tinkerVertex.id();
    }

    @Override
    public float getDistanceWeight() {
        return this.distanceWeight;
    }

    @Override
    public float getElevationWeight() {
        return this.elevationWeight;
    }

    @Override
    public List<AbstractElenaNode<T1, T2, E>> getNeighbors() {

        List<AbstractElenaNode<T1, T2, E>> neighbors = new ArrayList<>();

        for(AbstractElenaEdge<T1, T2, E> edge : this.getOutGoingEdges()){
            neighbors.add(edge.getDestinationNode());
        }
        return neighbors;
    }

    @Override
    public List<AbstractElenaEdge<T1, T2, E>> getOutGoingEdges() {

        List<AbstractElenaEdge<T1, T2, E>> outgoingEdges = new ArrayList<>();
        tinkerVertex.edges(Direction.OUT).forEachRemaining(edge -> {
            outgoingEdges.add(this.graph.getEdge((T2) edge.id()));
        });
        return outgoingEdges;
    }

    @Override
    public List<AbstractElenaEdge<T1, T2, E>> getIncomingEdges() {
        List<AbstractElenaEdge<T1, T2, E>> outgoingEdges = new ArrayList<>();
        tinkerVertex.edges(Direction.IN).forEachRemaining(edge -> {
            outgoingEdges.add(this.graph.getEdge((T2) edge.id()));
        });
        return outgoingEdges;
    }

    @Override
    public String getLatitude() {
        return null;
    }

    @Override
    public String getLongitude() {
        return null;
    }
}
