package com.elena.elena.model;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public class ElenaPath<T1,T2, E> extends AbstractElenaPath<T1, T2, E>{
    @Override
    public List<AbstractElenaEdge<T1, T2, E>> getEdgesInPath() {
        return null;
    }

    @Override
    public Map<String, BigDecimal> getPathWeights() {
        return null;
    }
}
