package com.elena.elena.routing;

import com.elena.elena.model.AbstractElenaGraph;
import com.elena.elena.model.AbstractElenaNode;
import com.elena.elena.model.AbstractElenaEdge;
import com.elena.elena.model.AbstractElenaPath;
import com.elena.elena.model.ElenaPath;

import java.util.List;
import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.Comparator;
import java.util.Map;
import java.util.HashMap;
import java.util.Collection;

public class DijkstraRouter extends AbstractRouter {

	private Map<AbstractElenaNode, AbstractElenaNode> nodeAncestor;

	// Constructor
	public DijkstraRouter() {
		this.nodeAncestor = new HashMap<>();
	}

    @Override
    public List<AbstractElenaPath> getRoute(AbstractElenaNode from, AbstractElenaNode to, AbstractElenaGraph graph) {
        
        // Initialize the graph
        this.initializeGraph(graph, from);

        // Define comparator for our self-defined AbstractElenaNode
        Comparator<AbstractElenaNode> nodeDistanceComparator = new Comparator<AbstractElenaNode> {
        	@Override
        	public int compare(AbstractElenaNode n1, AbstractElenaNode n2) {
        		if(n1.getDistanceWeight() > n2.getDistanceWeight())
        			return 1;
        		else if(n1.getDistanceWeight() < n2.getDistanceWeight())
        			return -1;
        		else
        			return 0;
        	}
        }

        // Initialize min-priority queue
        PriorityQueue<AbstractElenaNode> nodePriorityQueue = new PriorityQueue<>(nodeDistanceComparator);
        Collection<AbstractElenaNode> nodes = graph.getAllNodes();
        for(AbstractElenaNode node : nodes)
        	nodePriorityQueue.add(node);

        // Perform Dijkstra algorithm to find shortest path between specified source and destination
        while(true) {
        	AbstractElenaNode candidateNode = nodePriorityQueue.poll();
       		// Check if the shortest path from source to destination has been found
        	if(candidateNode == to) {
        		// Construct the path from the destination
        		List<AbstractElenaPath> shortestPaths = new ArrayList<AbstractElenaPath>();
        		AbstractElenaPath shortestPath = new ElenaPath();
        		AbstractElenaNode currentNode = candidateNode;
        		AbstractElenaEdge currentEdge = null;
        		while(this.nodeAncestor.containsKey(currentNode)) {
        			currentEdge = this.nodeAncestor.get(currentNode).getEdge(currentNode);
        			shortestPath.addEdgeToPath(0, currentEdge);
        			currentNode = this.nodeAncestor.get(currentNode);
        		}
        		// Return the shortest path
        		shortestPaths.add(shortestPath);
        		return shortestPaths;
        	}
        	// Perform relaxation if the shortest path from source to destination hasn't been found
        	else {
        		List<AbstractElenaEdge> edges = candidateNode.getOutGoingEdges();
        		for(AbstractElenaEdge edge : edges) {
        			AbstractElenaNode node = edge.getDestinationNode();
        			this.relaxEdge(candidateNode, node, edge.getEdgeDistance(), nodePriorityQueue);
        		}
        	}
        }
    }

    public void initializeGraph(AbstractElenaGraph graph, AbstractElenaNode from) {
    	
    	// Iterate through each node in graph to initialize them
    	Collection<AbstractElenaNode> nodes = graph.getAllNodes();
    	for(AbstractElenaNode node : nodes) {
    		node.setDistanceWeight(Float.MAX_VALUE);
    		this.nodeAncestor.put(node, null);
    	}

    	// Initialize source node
    	from.setDistanceWeight(0);
    }

    public void relaxEdge(AbstractElenaNode in, AbstractElenaNode out, Float weight, PriorityQueue<AbstractElenaNode> nodePriorityQueue) {
    	
    	// Check if we need to relax the distance for the out node
    	if(out.getDistanceWeight() > in.getDistanceWeight() + weight) {
    		// Decrease distance of out node in the min-priority queue
    		nodePriorityQueue.remove(out);
    		out.setDistanceWeight(in.getDistanceWeight()+weight);
    		this.nodeAncestor.put(out, in);
    		nodePriorityQueue.add(out);
    	}
    }
}
