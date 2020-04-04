package com.elena.elena.model;


import java.util.List;

public abstract class AbstractElenaNode {

    public abstract String getId();

    public abstract float getDistanceWeight();

    public abstract float getElevationWeight();

    public abstract List<AbstractElenaNode> getNeighbors();

    public abstract List<AbstractElenaEdge> getOutGoingEdges();

    public abstract List<AbstractElenaEdge> getIncomingEdges();

    public abstract String getLatitude();

    public abstract String getLongitude();
}


