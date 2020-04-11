package com.elena.elena.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
    public Map<String, Float> getPathWeights() {
        return null;
    }
    
    @Override
    public float getPathTotalWeight() {
    	float pathTotalWeight = 0;
    	for(AbstractElenaEdge edge : this.edgesInPath) {
    		pathTotalWeight = pathTotalWeight + edge.getEdgeDistance();
    	}
    	return pathTotalWeight;
    }

    @Override
    public void addEdgeToPath(int position, AbstractElenaEdge edge) {
        this.edgesInPath.add(position, edge);
    }
}
