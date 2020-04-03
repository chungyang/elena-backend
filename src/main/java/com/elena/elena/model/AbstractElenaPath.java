package com.elena.elena.model;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public abstract class AbstractElenaPath<T, E> {

    public abstract List<AbstractElenaEdge<T, E>> getEdgesInPath();

    public abstract Map<String, BigDecimal> getPathWeights();

}
