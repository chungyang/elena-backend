package com.elena.elena.model;

import org.apache.tinkerpop.gremlin.structure.Edge;
import org.apache.tinkerpop.gremlin.structure.Graph;
import org.apache.tinkerpop.gremlin.structure.Property;
import org.apache.tinkerpop.gremlin.tinkergraph.structure.TinkerVertex;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ElenaEdge<T1, T2, E> extends AbstractElenaEdge<T1, T2, E> {

    private final Edge tinkerEdge;
    private final AbstractElenaGraph<T1, T2, E> graph;
    private  Map<String, E> properties;

    private final String LENGTH__PROPERTY_KEY = "length";


    public  ElenaEdge(AbstractElenaGraph<T1, T2, E> graph, Edge tinkerEdge){
        this.tinkerEdge = tinkerEdge;
        this.graph = graph;
        properties = new HashMap<>();
        this.importProperties();
    }

    private void importProperties(){

        Iterator<Property<E>> properties = tinkerEdge.properties();

        while(properties.hasNext()){
            Property<E> property = properties.next();
            String key = property.key();
            E value = property.value();
            this.properties.put(key, value);
        }
    }

    @Override
    public T2 getId() {
        return (T2) tinkerEdge.id();
    }

    @Override
    public float getEdgeDistance() {
        return Float.parseFloat((String) tinkerEdge.property(LENGTH__PROPERTY_KEY).value());
    }

    @Override
    public float getEdgeElevation() {
        return 0;
    }

    @Override
    public AbstractElenaNode<T1, T2, E> getOriginNode() {
        T1 destinationNodeId = (T1) tinkerEdge.inVertex().id();
        return this.graph.getNode(destinationNodeId);
    }

    @Override
    public AbstractElenaNode<T1, T2, E> getDestinationNode() {
        T1 destinationNodeId = (T1) tinkerEdge.outVertex().id();
        return this.graph.getNode(destinationNodeId);
    }

    @Override
    public Map<String, E> getProperties() {
        return this.properties;
    }
}
