package com.elena.elena.model;


import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public abstract class AbstractElenaPath{

    public abstract List<AbstractElenaEdge> getEdgesInPath();

    public abstract Map<String, BigDecimal> getPathWeights();

}