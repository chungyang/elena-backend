package com.elena.elena.model;


import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * This AbstractEdge class represents a uni-directional edge
 */
public abstract class AbstractElenaEdge<T, E> {


    public abstract T getId();

    public abstract float getEdgeDistance();

    public abstract float getEdgeElevation();

    public abstract AbstractElenaNode getOriginNode();

    public abstract AbstractElenaNode getDestinationNode();

    public abstract Map<String, E> getProperties();

    public List<AbstractElenaNode> getNodes(){
        List<AbstractElenaNode> nodes = new ArrayList<>();
        nodes.add(this.getOriginNode());
        nodes.add(this.getDestinationNode());
        return nodes;
    }

}
