package com.elena.elena.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

import com.elena.elena.routing.Weight;

public class ElenaPath extends AbstractElenaPath{

    private List<AbstractElenaEdge> edgesInPath;

    public ElenaPath(){
        edgesInPath = new ArrayList<>();
    }

    @Override
    public List<AbstractElenaEdge> getEdgesInPath() {
        return this.edgesInPath;
    }

    @Override
    public Map<Weight, Float> getPathWeights() {
    	
    	// Use a hash map to store different weights in a path
    	Map<Weight, Float> pathWeights = new HashMap<>();
    	
    	// Distance weight
        Weight distance = Weight.getWeightByName("distance");
        float pathDistance = 0;
        for(AbstractElenaEdge edge : this.edgesInPath) {
        	pathDistance = pathDistance + edge.getEdgeDistance();
        }
        pathWeights.put(distance, pathDistance);
        
        return pathWeights;        
    }

    @Override
    public void addEdgeToPath(int position, AbstractElenaEdge edge) {
        this.edgesInPath.add(position, edge);
    }
}