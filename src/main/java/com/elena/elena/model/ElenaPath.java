package com.elena.elena.model;

import java.util.*;

import com.elena.elena.routing.WeightType;
import com.fasterxml.jackson.annotation.JsonValue;

public class ElenaPath extends AbstractElenaPath{

    private List<AbstractElenaEdge> edgesInPath;
    private final String GEOMETRY_STRING_PREFIX = "LINESTRING ";
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
        stringBuilder.append("{ \"values\": [");

        for(AbstractElenaEdge edge : this.edgesInPath){

            String[] coordinates = edge.getProperties().getOrDefault("geometry", "")
                    .substring(GEOMETRY_STRING_PREFIX.length()).replaceAll("[(),]", "").split(" ");

            if(coordinates.length % 2 != 0){
                throw new IllegalStateException("Parsed coordinates should be divisible by 2");
            }

            //Leaflet accepts (lat,lon) pairs instead of (lon,lat) so we need to change the order here
            //since openstreetmap stores geometry in (lon,lat)
            for(int i = 0; i < coordinates.length; i+=2){
                stringBuilder.append("[").append(coordinates[i+1]).append(", ").append(coordinates[i]).append("],");
            }
        }

        return stringBuilder.deleteCharAt(stringBuilder.length() - 1).append("]}").toString();
    }
}