package com.elena.elena.model;


import lombok.Getter;
import lombok.Setter;

import java.util.List;

public abstract class ElenaNode {

    @Getter protected String id;

    @Setter @Getter protected float distanceWeight;

    @Getter @Setter protected float elevationWeight;

    @Getter protected List<ElenaNode> neighbors;

    @Getter protected List<ElenaEdge> outgoingEdges;

    @Getter protected List<ElenaEdge> incomingEdges;
}


