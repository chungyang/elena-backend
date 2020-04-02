package com.elena.elena.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

public abstract class AbstractElenaPath {

    @Getter @Setter protected List<AbstractElenaEdge> path;

    protected float getPathDistance(){

        float distance = 0;

        for(AbstractElenaEdge e : this.path){
            distance += e.edgeDistance;
        }

        return distance;
    }

    protected float getPathElevation(){

        float elevation = 0;

        for(AbstractElenaEdge e : this.path){
            elevation += e.edgeElevation;
        }

        return elevation;
    }
}
