package com.elena.elena.model;


import java.util.List;
import java.util.Map;

public abstract class AbstractElenaPath{

    public abstract List<AbstractElenaEdge> getEdgesInPath();

    public abstract Map<String, Float> getPathWeights();

    public abstract void addEdgeToPath(AbstractElenaEdge edge);

}
