package com.elena.elena.model;


import java.util.List;

public abstract class AbstractEdge {


    public abstract float getEdgeDistance();

    public abstract float getEdgeElevation();

    public abstract List<AbstractNode> getNodes();
}
