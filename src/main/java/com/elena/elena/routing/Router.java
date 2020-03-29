package com.elena.elena.routing;


import com.elena.elena.model.Coordinate;
import com.elena.elena.model.Node;

import java.util.List;

public abstract class Router {

    /**
     * This method should implement a routing algorithm and returns
     * a list of coordinates.
     */
    public abstract List<Coordinate> getRoute(Node from, Node to);

}
