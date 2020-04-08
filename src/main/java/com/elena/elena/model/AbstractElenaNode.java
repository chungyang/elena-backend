package com.elena.elena.model;


import lombok.Setter;

import java.util.Collection;
import java.util.List;
import java.util.Optional;


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

    public abstract Collection<AbstractElenaNode> getNeighbors();

    public abstract Collection<AbstractElenaEdge> getOutGoingEdges();

    public abstract Collection<AbstractElenaEdge> getIncomingEdges();

    public abstract String getLatitude();

    public abstract String getLongitude();

    public abstract Optional<AbstractElenaEdge> getEdge(AbstractElenaNode destinationNode);

}


