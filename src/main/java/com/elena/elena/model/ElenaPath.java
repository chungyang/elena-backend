package com.elena.elena.model;

import java.util.*;

import com.elena.elena.routing.WeightType;
import com.fasterxml.jackson.annotation.JsonValue;

public class ElenaPath extends AbstractElenaPath{

    private List<AbstractElenaEdge> edgesInPath;
    private Map<WeightType, Float> weights;
    private final String GEOMETRY_STRING_PREFIX = "LINESTRING ";

    public ElenaPath(){
        edgesInPath = new LinkedList<>();
        weights = new HashMap<>();
    }

    @Override
    public List<AbstractElenaEdge> getEdgesInPath() {
        return this.edgesInPath;
    }

    @Override
    public Map<WeightType, Float> getPathWeights() {
    	
    	// Use a hash map to store different weights in a path
    	Map<WeightType, Float> pathWeights = new HashMap<>();
    	
    	// Distance weight
        float pathDistance = 0;
        float pathElevation = 0;
        for(AbstractElenaEdge edge : this.edgesInPath) {
        	pathDistance += edge.getEdgeDistance();
        	pathElevation += edge.getEdgeElevation();
        }
        pathWeights.put(WeightType.DISTANCE, pathDistance);
        pathWeights.put(WeightType.ELEVATION, pathElevation);

        return weights;
    }

    @Override
    public void addEdgeToPath(int position, AbstractElenaEdge edge) {
        this.edgesInPath.add(position, edge);
    }


    @Override
    @JsonValue
    public String toString(){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("{ \"values\":");

        for(AbstractElenaEdge edge : this.edgesInPath){

            String[] coordinates = edge.getProperties().getOrDefault("geometry", "")
                    .substring(GEOMETRY_STRING_PREFIX.length()).replaceAll("[(),]", "").split(" ");

            if(coordinates.length % 2 != 0){
                throw new IllegalStateException("Parsed coordinates should be divisible by 2");
            }
            

        }

        return stringBuilder.toString();
    }
}