package com.elena.elena.model;

import org.apache.tinkerpop.gremlin.structure.Edge;
import org.apache.tinkerpop.gremlin.structure.Property;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ElenaEdge extends AbstractElenaEdge {

    private final Edge tinkerEdge;
    private final AbstractElenaGraph graph;
    private  Map<String, String> properties;
    private float edgeDistance;

    private final String LENGTH__PROPERTY_KEY = "length";

    public  ElenaEdge(AbstractElenaGraph graph, Edge tinkerEdge){
        this.tinkerEdge = tinkerEdge;
        this.graph = graph;
        properties = new HashMap<>();
        this.importProperties();
        this.edgeDistance = Float.parseFloat((String) tinkerEdge.property(LENGTH__PROPERTY_KEY).value());
    }

    private void importProperties(){

        Iterator<Property<String>> properties = tinkerEdge.properties();

        while(properties.hasNext()){
            Property<String> property = properties.next();
            String key = property.key();
            String value = property.value();
            this.properties.put(key, value);
        }
    }

    @Override
    public String getId() {
        return (String) tinkerEdge.id();
    }

    @Override
    public float getEdgeDistance() {
        return this.edgeDistance;
    }
    
    @Override
    public void setEdgeDistance(float distance) {
    	this.edgeDistance = distance;
    }

    @Override
    public float getEdgeElevation() {
        return 0;
    }

    @Override
    public AbstractElenaNode getOriginNode() {
        String originNodeId = (String) tinkerEdge.outVertex().id();
        return this.graph.getNode(originNodeId).get();
    }

    @Override
    public AbstractElenaNode getDestinationNode() {
        String destinationNodeId = (String) tinkerEdge.inVertex().id();
        return this.graph.getNode(destinationNodeId).get();
    }

    @Override
    public Map<String, String> getProperties() {
        return this.properties;
    }
}