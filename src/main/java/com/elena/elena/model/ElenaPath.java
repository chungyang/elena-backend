package com.elena.elena.model;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public class ElenaPath<T, E> extends AbstractElenaPath<T, E>{
    @Override
    public List<AbstractElenaEdge<T, E>> getEdgesInPath() {
        return null;
    }

    @Override
    public Map<String, BigDecimal> getPathWeights() {
        return null;
    }
}
