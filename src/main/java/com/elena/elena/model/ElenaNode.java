package com.elena.elena.model;

import com.elena.elena.dao.ElevationData;
import org.apache.tinkerpop.gremlin.structure.Direction;
import org.apache.tinkerpop.gremlin.structure.Vertex;

import java.util.*;

public class ElenaNode extends AbstractElenaNode {

    private final Vertex tinkerVertex;
    private final AbstractElenaGraph graph;
    private Map<AbstractElenaNode, AbstractElenaEdge> outgoingEdges = new HashMap();
    private List<AbstractElenaEdge> incomingEdges = new ArrayList<>();
    private List<AbstractElenaNode> neighbors = new ArrayList<>();

    public ElenaNode(AbstractElenaGraph graph, Vertex tinkerVertex){

        this.graph = graph;
        this.tinkerVertex = tinkerVertex;
    }

    @Override
    public String getId() {
        return (String) tinkerVertex.id();
    }

    @Override
    public Float getDistanceWeight() {
        return this.distanceWeight;
    }

    @Override
    public Float getElevationWeight() {
        return this.elevationWeight;
    }

    @Override
    public Collection<AbstractElenaNode> getNeighbors() {

        if(neighbors.isEmpty()) {
            for (AbstractElenaEdge edge : this.getOutGoingEdges()) {
                neighbors.add(edge.getDestinationNode());
            }
        }
        return neighbors;
    }

    @Override
    public Collection<AbstractElenaEdge> getOutGoingEdges() {

        if(outgoingEdges.isEmpty()) {
            tinkerVertex.edges(Direction.OUT).forEachRemaining(edge -> {
                outgoingEdges.put(this.graph.getNode((String) edge.inVertex().id()).get(),
                        this.graph.getEdge((String) edge.id()));
            });
        }
        return outgoingEdges.values();
    }

    @Override
    public Collection<AbstractElenaEdge> getInComingEdges() {

        if(incomingEdges.isEmpty()) {
            tinkerVertex.edges(Direction.IN).forEachRemaining(edge -> {
                incomingEdges.add(this.graph.getEdge((String) edge.id()));
            });
        }
        return incomingEdges;
    }

    @Override
    public String getLatitude() {
        return this.tinkerVertex.property("lat").value().toString();
    }

    @Override
    public String getLongitude() {
        return this.tinkerVertex.property("lon").value().toString();
    }

    @Override
    public Optional<AbstractElenaEdge> getEdge(AbstractElenaNode destinationNode) {
        return Optional.ofNullable(outgoingEdges.getOrDefault(destinationNode, null));
    }

    @Override
    public boolean equals(Object o) {

        if(o == this){
            return true;
        }
        ElenaNode node = (ElenaNode) o;

        return node.getId().equals(this.getId());
    }

    @Override
    public int hashCode(){
        return this.getId().hashCode();
    }
}