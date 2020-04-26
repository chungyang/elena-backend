package com.elena.elena.routing;

import com.elena.elena.model.*;
import com.elena.elena.util.ElenaUtils;
import com.elena.elena.util.Units;

import java.util.*;

public class DijkstraElevationRouter extends AbstractRouter{

    private float shortestPathPercentage;
    private ElevationMode elevationMode;
    private Map<AbstractElenaNode, AbstractElenaNode> nodeAncestor;
    private Map<AbstractElenaNode, Float> nodeTentativeElevation;
    private Map<AbstractElenaNode, PriorityQueue<NodeWrapper>> potentialCandidates;

    protected DijkstraElevationRouter(int percentage, ElevationMode elevationMode){

        this.shortestPathPercentage = percentage;
        this.elevationMode = elevationMode;
        this.nodeAncestor = new HashMap<>();
        this.nodeTentativeElevation = new HashMap<>();
        potentialCandidates = new HashMap<>();
    }

    @Override
    public List<AbstractElenaPath> getRoute(AbstractElenaNode originNode, AbstractElenaNode destinationNode, AbstractElenaGraph graph) {

        AbstractRouter distanceRouter = RouterFactory.getRouter(Algorithm.A_STAR);
        AbstractElenaPath shortestPath = distanceRouter.getRoute(originNode, destinationNode, graph).get(0);
        List<AbstractElenaPath> selectedPath = new LinkedList<>();
        float distanceMargin = shortestPath.getPathWeights().get(WeightType.DISTANCE) * this.shortestPathPercentage / 100;

        float initialElevation = this.elevationMode == ElevationMode.MIN? Float.MAX_VALUE : -Float.MAX_VALUE;
        this.nodeTentativeElevation.put(originNode, 0f);

        //Priority is a node's current tentative cumulative elevation gain
        PriorityQueue<NodeWrapper> nodePriorityQueue = this.getMinNodePriorityQueue();
        nodePriorityQueue.add(new ElevatioNodeWrapper(0f, 0f, originNode));

        while(!nodePriorityQueue.isEmpty()) {

            ElevatioNodeWrapper nodeWrapper = (ElevatioNodeWrapper) nodePriorityQueue.poll();
            AbstractElenaNode candidateNode = nodeWrapper.wrappedNode;

            if(this.nodeTentativeElevation.get(candidateNode) > nodeWrapper.weight){
                this.nodeTentativeElevation.put(candidateNode, nodeWrapper.weight);
            }
            // Check if the max/min path from source to destination has been found
            if(candidateNode == destinationNode) {
                // Construct the path from the destination
                AbstractElenaPath path = new ElenaPath();
                AbstractElenaNode currentNode = candidateNode;
                Optional<AbstractElenaEdge> currentEdge;
                this.nodeAncestor.put(originNode, null);
                while(this.nodeAncestor.get(currentNode) != null) {
                    currentEdge = this.nodeAncestor.get(currentNode).getEdge(currentNode);
                    path.addEdgeToPath(0, currentEdge.get());
                    currentNode = this.nodeAncestor.get(currentNode);
                }
                // Return the max/min elevation path
                selectedPath.add(path);
                return selectedPath;
            }
            else {
                Collection<AbstractElenaEdge> edges = candidateNode.getOutGoingEdges();
                boolean hasNextNodeOnPath = false;

                for(AbstractElenaEdge edge : edges) {
                    AbstractElenaNode node = edge.getDestinationNode();

                    hasNextNodeOnPath |= this.relaxEdge(nodeWrapper, node, destinationNode, distanceMargin, edge.getEdgeDistance(),
                            edge.getEdgeElevation(), initialElevation,  nodePriorityQueue);
                }

                if(!hasNextNodeOnPath){
                    this.nodeTentativeElevation.put(candidateNode, initialElevation);
                    if(this.potentialCandidates.containsKey(candidateNode) && !this.potentialCandidates.get(candidateNode).isEmpty()) {
                        nodePriorityQueue.add(this.potentialCandidates.get(candidateNode).poll());
                    }
                }
            }
        }


        return selectedPath;
    }

    private boolean relaxEdge(ElevatioNodeWrapper sourceNodeWrapper, AbstractElenaNode targetNode,
                              AbstractElenaNode destination, float distanceMargin,
                              float edgeDistance, float edgeElevation, float initialElevation, PriorityQueue<NodeWrapper> queue){

        float straightLineDistance = ElenaUtils.getDistance(targetNode, destination, Units.METRIC);

        ElevatioNodeWrapper elevatioNodeWrapper = new ElevatioNodeWrapper(
                this.elevationMode.sign * (sourceNodeWrapper.weight + edgeElevation),
                sourceNodeWrapper.tentativeDistance + edgeDistance, targetNode);

        if (!(sourceNodeWrapper.tentativeDistance + edgeDistance + straightLineDistance > distanceMargin) &&
                this.nodeAncestor.get(sourceNodeWrapper.wrappedNode) != targetNode &&
                        elevationMode.sign * this.nodeTentativeElevation.getOrDefault(targetNode, initialElevation) >
                                (this.nodeTentativeElevation.get(sourceNodeWrapper.wrappedNode) + edgeElevation) * elevationMode.sign) {

            this.nodeTentativeElevation.put(targetNode, this.nodeTentativeElevation.get(sourceNodeWrapper.wrappedNode) + edgeElevation);

            queue.add(elevatioNodeWrapper);
            this.nodeAncestor.put(targetNode, sourceNodeWrapper.wrappedNode);
            return true;

        }
        else if(!(sourceNodeWrapper.tentativeDistance + edgeDistance + straightLineDistance> distanceMargin)){

            potentialCandidates.putIfAbsent(targetNode, this.getMinNodePriorityQueue());
            potentialCandidates.get(targetNode).add(elevatioNodeWrapper);
        }

        return false;
    }

    //Its super class's weight is the value that indicates its priority in the queue. In case
    //of elevation router, it needs to be the elevation.
    private class ElevatioNodeWrapper extends NodeWrapper{

        float tentativeDistance;
        ElevatioNodeWrapper(float tentativeElevation, float tentativeDistance, AbstractElenaNode node){

            super(node, tentativeElevation);
            this.tentativeDistance = tentativeDistance;
        }
    }

}
