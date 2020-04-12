package com.elena.elena.model;

import com.elena.elena.routing.Weight;

import java.util.List;
import java.util.Map;

public abstract class AbstractElenaPath{

    public abstract List<AbstractElenaEdge> getEdgesInPath();

    public abstract Map<Weight, Float> getPathWeights();

    public abstract void addEdgeToPath(int position, AbstractElenaEdge edge);
}