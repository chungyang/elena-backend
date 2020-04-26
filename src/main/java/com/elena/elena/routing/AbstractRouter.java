package com.elena.elena.routing;


import com.elena.elena.model.AbstractElenaGraph;
import com.elena.elena.model.AbstractElenaNode;
import com.elena.elena.model.AbstractElenaPath;

import java.util.List;
import java.util.PriorityQueue;

public abstract class AbstractRouter {

    /**
     * This method should implement a com.elena.elena.routing algorithm and returns
     * a list of paths
     */
    public abstract List<AbstractElenaPath> getRoute(AbstractElenaNode originNode, AbstractElenaNode destinationNode, AbstractElenaGraph graph);

    protected PriorityQueue<NodeWrapper> getMinNodePriorityQueue(){

        return new PriorityQueue<>((n1 , n2) -> {
            if(n1.weight > n2.weight)
                return 1;
            else if(n1.weight < n2.weight)
                return -1;
            else
                return 0;
        });
    }

    protected class NodeWrapper{

        AbstractElenaNode wrappedNode;
        Float weight;

        // Constructor
        public NodeWrapper(AbstractElenaNode node, Float weight) {
            this.wrappedNode = node;
            this.weight = weight;
        }
    }

}
