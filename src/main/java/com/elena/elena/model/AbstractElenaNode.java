package com.elena.elena.model;


import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.util.List;

public abstract class AbstractElenaNode<T> {

    @Getter @NonNull protected T id;

    @Setter @Getter protected float distanceWeight;

    @Getter @Setter protected float elevationWeight;

    @Getter protected List<AbstractElenaNode> neighbors;

    @Getter protected List<AbstractElenaEdge> outgoingEdges;

    @Getter protected List<AbstractElenaEdge> incomingEdges;
}


