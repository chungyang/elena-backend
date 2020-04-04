package com.elena.elena.model;


import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public abstract class AbstractElenaPath<T1,T2, E> {

    public abstract List<AbstractElenaEdge<T1, T2, E>> getEdgesInPath();

    public abstract Map<String, BigDecimal> getPathWeights();

}
