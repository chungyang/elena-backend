package com.elena.elena.model;

import org.apache.tinkerpop.gremlin.structure.Direction;
import org.apache.tinkerpop.gremlin.structure.Vertex;

import java.util.ArrayList;
import java.util.List;

public class ElenaNode extends AbstractElenaNode {

    private final Vertex tinkerVertex;
    private final AbstractElenaGraph graph;
    private float distanceWeight;
    private float elevationWeight;

    public ElenaNode(AbstractElenaGraph graph, Vertex tinkerVertex){

        this.graph = graph;
        this.tinkerVertex = tinkerVertex;
    }

    @Override
    public String getId() {
        return (String) tinkerVertex.id();
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
    public List<AbstractElenaNode> getNeighbors() {

        List<AbstractElenaNode> neighbors = new ArrayList<>();

        for(AbstractElenaEdge edge : this.getOutGoingEdges()){
            neighbors.add(edge.getDestinationNode());
        }
        return neighbors;
    }

    @Override
    public List<AbstractElenaEdge> getOutGoingEdges() {

        List<AbstractElenaEdge> outgoingEdges = new ArrayList<>();
        tinkerVertex.edges(Direction.OUT).forEachRemaining(edge -> {
            outgoingEdges.add(this.graph.getEdge((String) edge.id()));
        });
        return outgoingEdges;
    }

    @Override
    public List<AbstractElenaEdge> getIncomingEdges() {
        List<AbstractElenaEdge> outgoingEdges = new ArrayList<>();
        tinkerVertex.edges(Direction.IN).forEachRemaining(edge -> {
            outgoingEdges.add(this.graph.getEdge((String) edge.id()));
        });
        return outgoingEdges;
    }

    @Override
    public String getLatitude() {
        return this.tinkerVertex.property("lat").value().toString();
    }

    @Override
    public String getLongitude() {
        return this.tinkerVertex.property("lon").value().toString();
    }
}
