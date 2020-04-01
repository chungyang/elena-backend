package com.elena.elena.model;


import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * This AbstractEdge class represents a uni-directional edge
 */
public abstract class ElenaEdge {

    @Getter protected String id;

    @Getter @Setter protected float edgeDistance;

    @Getter @Setter protected float edgeElevation;

    @Getter protected ElenaNode originNode;

    @Getter protected ElenaNode destinationNode;

    /**
     * An edge may contain information such as its length, its id in openstreetmap, etc
     */
    @Getter protected Map<String, Object> properties;

    public List<ElenaNode> getNodes(){
        List<ElenaNode> nodes = new ArrayList<>();
        nodes.add(originNode);
        nodes.add(destinationNode);
        return nodes;
    }

}
