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
	private Comparator<NodeWrapper> comparator;

	// Constructor
	public DijkstraRouter() {

		this.nodeAncestor = new HashMap<>();
		this.comparator = (n1 , n2) -> {
			if(n1.getDistanceWeight() > n2.getDistanceWeight())
				return 1;
			else if(n1.getDistanceWeight() < n2.getDistanceWeight())
				return -1;
			else
				return 0;
		};
	}
	
	public class NodeWrapper {
		
		AbstractElenaNode wrappedNode;
		Float distanceWeight;
		
		// Constructor
		public NodeWrapper(AbstractElenaNode node) {
			this.wrappedNode = node;
			this.distanceWeight = node.getDistanceWeight();
		}
		
		public Float getDistanceWeight() {
			return this.distanceWeight;
		}
		
		public AbstractElenaNode getNode() {
			return this.wrappedNode;
		}
	}

	@Override
	public List<AbstractElenaPath> getRoute(AbstractElenaNode from, AbstractElenaNode to, AbstractElenaGraph graph) {

		// Initialize list to record shortest path
		List<AbstractElenaPath> shortestPaths = new ArrayList<>();
		
		// Initialize graph
		this.initializeGraph(graph, from);
		
		// Initialize min-priority queue
		PriorityQueue<NodeWrapper> nodePriorityQueue = new PriorityQueue<>(comparator);
		nodePriorityQueue.add(new NodeWrapper(from));

		// Perform Dijkstra algorithm to find shortest path between specified source and destination
		while(!nodePriorityQueue.isEmpty()) {
			AbstractElenaNode candidateNode = nodePriorityQueue.poll().getNode();
			// Check if the shortest path from source to destination has been found
			if(candidateNode == to) {
				// Construct the path from the destination
				AbstractElenaPath shortestPath = new ElenaPath();
				AbstractElenaNode currentNode = candidateNode;
				Optional<AbstractElenaEdge> currentEdge;
				while(this.nodeAncestor.get(currentNode) != null) {
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

	private void initializeGraph(AbstractElenaGraph graph, AbstractElenaNode from) {

		// Iterate through each node in graph to initialize them
		Collection<AbstractElenaNode> nodes = graph.getAllNodes();
		for(AbstractElenaNode node : nodes) {
			node.setDistanceWeight(Float.MAX_VALUE);
		}

		// Initialize source node
		from.setDistanceWeight(0f);
		this.nodeAncestor.put(from, null);
	}

	public void relaxEdge(AbstractElenaNode in, AbstractElenaNode out, Float weight, PriorityQueue<NodeWrapper> nodePriorityQueue) {

		// Check if we need to relax the distance for the out node
		if(out.getDistanceWeight() > in.getDistanceWeight() + weight) {
			// Decrease distance of out node in the min-priority queue
			out.setDistanceWeight(in.getDistanceWeight()+weight);
			this.nodeAncestor.put(out, in);
			// Wrap the node to maintain order in min-priority queue
			NodeWrapper wrappedOutNode = new NodeWrapper(out);
			nodePriorityQueue.add(wrappedOutNode);
		}
	}

}