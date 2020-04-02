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
public abstract class AbstractElenaEdge<E> {

    @Getter @NonNull protected String id;

    @Getter @Setter protected float edgeDistance;

    @Getter @Setter protected float edgeElevation;

    @Getter protected AbstractElenaNode originNode;

    @Getter protected AbstractElenaNode destinationNode;

    /**
     * An edge may contain information such as its length, its id in openstreetmap, etc
     */
    @Getter protected Map<String, E> properties;

    public List<AbstractElenaNode> getNodes(){
        List<AbstractElenaNode> nodes = new ArrayList<>();
        nodes.add(originNode);
        nodes.add(destinationNode);
        return nodes;
    }

}
