package com.elena.elena.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

import com.elena.elena.routing.WeightType;

public class ElenaPath extends AbstractElenaPath{

    private List<AbstractElenaEdge> edgesInPath;
    private Map<WeightType, Float> weights;

    public ElenaPath(){
        edgesInPath = new ArrayList<>();
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
        pathWeights.put(WeightType.ELEVATION, pathDistance);

        return weights;
    }

    @Override
    public void addEdgeToPath(int position, AbstractElenaEdge edge) {
        this.edgesInPath.add(position, edge);
    }
}