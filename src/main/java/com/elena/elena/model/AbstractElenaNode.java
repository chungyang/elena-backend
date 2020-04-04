package com.elena.elena.model;


import lombok.Setter;

import java.util.List;


public abstract class AbstractElenaNode {

    @Setter protected Float distanceWeight = Float.MAX_VALUE;

    /**
     * The initial elevation is set to null, concrete implementation
     * could populate it at creation
     */
    @Setter protected Float elevationWeight;

    public abstract String getId();

    public abstract Float getDistanceWeight();

    public abstract Float getElevationWeight();

    public abstract List<AbstractElenaNode> getNeighbors();

    public abstract List<AbstractElenaEdge> getOutGoingEdges();

    public abstract List<AbstractElenaEdge> getIncomingEdges();

    public abstract String getLatitude();

    public abstract String getLongitude();

}


