package com.elena.elena.model;

import com.elena.elena.routing.WeightType;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ElenaPath extends AbstractElenaPath{

    private List<AbstractElenaEdge> edgesInPath;
    private Map<WeightType, Float> pathWeights;

    public ElenaPath(){
        edgesInPath = new LinkedList<>();
        pathWeights = new HashMap<>();
    }

    @Override
    public List<AbstractElenaEdge> getEdgesInPath() {
        return this.edgesInPath;
    }

    @Override
    public Map<WeightType, Float> getPathWeights() {

        if(pathWeights.isEmpty()) {
            // Distance weight
            float pathDistance = 0;
            float pathElevation = 0;
            for (AbstractElenaEdge edge : this.edgesInPath) {
                pathDistance += edge.getEdgeDistance();
                pathElevation += edge.getEdgeElevation();
            }
            pathWeights.put(WeightType.DISTANCE, pathDistance);
            pathWeights.put(WeightType.ELEVATION, pathElevation);
        }

        return pathWeights;
    }

    @Override
    public void addEdgeToPath(int position, AbstractElenaEdge edge) {
        this.edgesInPath.add(position, edge);
    }


    @Override
    @JsonValue
    public String toString(){

        StringBuilder stringBuilder = new StringBuilder();

        for(AbstractElenaEdge edge : this.edgesInPath){
            stringBuilder.append(edge.toString()).append(",");
        }

        return  String.format("{ \"values\": [ %s ], \"distance\": %s, \"elevation\": %s}",
                stringBuilder.deleteCharAt(stringBuilder.length() - 1).toString(),
                this.getPathWeights().get(WeightType.DISTANCE),
                this.getPathWeights().get(WeightType.ELEVATION));
    }
}