package com.elena.elena.model;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * This AbstractEdge class represents a uni-directional edge
 */
public abstract class AbstractElenaEdge<T1, T2, E> {


    public abstract T2 getId();

    public abstract float getEdgeDistance();

    public abstract float getEdgeElevation();

    public abstract AbstractElenaNode<T1, T2, E> getOriginNode();

    public abstract AbstractElenaNode<T1, T2, E> getDestinationNode();

    public abstract Map<String, E> getProperties();

    public List<AbstractElenaNode<T1, T2, E>> getNodes(){
        List<AbstractElenaNode<T1, T2, E>> nodes = new ArrayList<>();
        nodes.add(this.getOriginNode());
        nodes.add(this.getDestinationNode());
        return nodes;
    }

}
