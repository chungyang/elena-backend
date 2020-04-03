package com.elena.elena.model;

import lombok.Getter;

import java.util.Map;

public abstract class AbstractElenaGraph<T1, T2, E> {

    public abstract AbstractElenaNode<T1> getCurrentNode();

    public abstract AbstractElenaNode<T1> getNode(T1 id);

    public abstract AbstractElenaEdge<T2, E> getEdge(T2 id);

}
