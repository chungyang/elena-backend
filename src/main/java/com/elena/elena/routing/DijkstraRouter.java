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
import java.util.Optional;

public class DijkstraRouter extends AbstractRouter {

	private Map<AbstractElenaNode, AbstractElenaNode> nodeAncestor;
	private Comparator<AbstractElenaNode> comparator;

	// Constructor
	public DijkstraRouter() {

		this.nodeAncestor = new HashMap<>();
		comparator = (n1 , n2) ->{
			if(n1.getDistanceWeight() > n2.getDistanceWeight())
				return 1;
			else if(n1.getDistanceWeight() < n2.getDistanceWeight())
				return -1;
			else
				return 0;};
	}

	@Override
	public List<AbstractElenaPath> getRoute(AbstractElenaNode from, AbstractElenaNode to, AbstractElenaGraph graph) {

		// Initialize list to record shortest path
		List<AbstractElenaPath> shortestPaths = new ArrayList<>();
		from.setDistanceWeight(0f);
		nodeAncestor.put(from, null);

		// Initialize min-priority queue
		PriorityQueue<AbstractElenaNode> nodePriorityQueue = new PriorityQueue<>(comparator);
		nodePriorityQueue.add(from);

		// Perform Dijkstra algorithm to find shortest path between specified source and destination
		while(!nodePriorityQueue.isEmpty()) {
			AbstractElenaNode candidateNode = nodePriorityQueue.poll();
			// Check if there is no path to return
			if(candidateNode.getDistanceWeight() == Float.MAX_VALUE)
				return shortestPaths;
			// Check if the shortest path from source to destination has been found
			if(candidateNode == to) {
				// Construct the path from the destination
				AbstractElenaPath shortestPath = new ElenaPath();
				AbstractElenaNode currentNode = candidateNode;
				Optional<AbstractElenaEdge> currentEdge;
				while(this.nodeAncestor.containsKey(currentNode) && this.nodeAncestor.get(currentNode)!=null) {
					currentEdge = this.nodeAncestor.get(currentNode).getEdge(currentNode);
					shortestPath.addEdgeToPath(0, currentEdge.get());
					currentNode = this.nodeAncestor.get(currentNode);
				}
				// Return the shortest path
				shortestPaths.add(shortestPath);
				return shortestPaths;
			}
			// Perform relaxation if the shortest path from source to destination hasn't been found
			else {
				Collection<AbstractElenaEdge> edges = candidateNode.getOutGoingEdges();
				for(AbstractElenaEdge edge : edges) {
					AbstractElenaNode node = edge.getDestinationNode();
					this.relaxEdge(candidateNode, node, edge.getEdgeDistance(), nodePriorityQueue);
				}
			}
		}

		return shortestPaths;
	}


	public void relaxEdge(AbstractElenaNode in, AbstractElenaNode out, Float weight, PriorityQueue<AbstractElenaNode> nodePriorityQueue) {

		// Check if we need to relax the distance for the out node
		if(out.getDistanceWeight() > in.getDistanceWeight() + weight) {
			// Decrease distance of out node in the min-priority queue
			out.setDistanceWeight(in.getDistanceWeight()+weight);
			this.nodeAncestor.put(out, in);
			nodePriorityQueue.add(out);
		}
	}
}