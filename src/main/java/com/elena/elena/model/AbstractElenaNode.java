package com.elena.elena.model;


import java.util.List;

public abstract class AbstractElenaNode<T1, T2, E> {

    public abstract T1 getId();

    public abstract float getDistanceWeight();

    public abstract float getElevationWeight();

    public abstract List<AbstractElenaNode<T1, T2, E>> getNeighbors();

    public abstract List<AbstractElenaEdge<T1, T2, E>> getOutGoingEdges();

    public abstract List<AbstractElenaEdge<T1, T2, E>> getIncomingEdges();

    public abstract String getLatitude();

    public abstract String getLongitude();
}


