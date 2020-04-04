package com.elena.elena.routing;


import com.elena.elena.model.AbstractElenaGraph;
import com.elena.elena.model.AbstractElenaPath;

import java.util.List;

public abstract class AbstractRouter {

    /**
     * This method should implement a routing algorithm and returns
     * a list of paths
     */
    public abstract List<AbstractElenaPath> getRoute(String originNodeId, String destinationNodeId, AbstractElenaGraph graph);

}