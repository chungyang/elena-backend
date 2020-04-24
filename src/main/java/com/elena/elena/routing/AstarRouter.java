package com.elena.elena.routing;

import com.elena.elena.model.AbstractElenaGraph;
import com.elena.elena.model.AbstractElenaNode;
import com.elena.elena.model.AbstractElenaEdge;
import com.elena.elena.model.AbstractElenaPath;
import com.elena.elena.model.ElenaPath;
import com.elena.elena.util.ElenaUtils;
import com.elena.elena.util.Units;

import java.util.List;
import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.Map;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Collection;
import java.util.Optional;

public class AstarRouter extends AbstractRouter{

	private Map<AbstractElenaNode, AbstractElenaNode> nodeAncestor;
	private Map<AbstractElenaNode, Float> gScores; // Distance between a node and origin
	private Map<AbstractElenaNode, Float> hScores; // Heuristic cost from a node to destination

	// Constructor
	public AstarRouter() {
		this.nodeAncestor = new HashMap<>();
		this.gScores = new HashMap<>();
		this.hScores = new HashMap<>();
	}

	private class NodeWrapper {

		AbstractElenaNode wrappedNode;
		Float distanceWeight;

		// Constructor
		public NodeWrapper(AbstractElenaNode node, Float distanceWeight) {
			this.wrappedNode = node;
			this.distanceWeight = distanceWeight;
		}
	}

	@Override
	public List<AbstractElenaPath> getRoute(AbstractElenaNode from, AbstractElenaNode to, AbstractElenaGraph graph) {
		// Initialize list to record shortest path
		List<AbstractElenaPath> shortestPaths = new ArrayList<>();

		// Initialize graph
		this.initializeGraph(graph, from, to);
		this.gScores.put(from, 0f);

		// Initialize open list
		PriorityQueue<NodeWrapper> nodePriorityQueue = new PriorityQueue<>((n1 , n2) -> {
			if(n1.distanceWeight > n2.distanceWeight)
				return 1;
			else if(n1.distanceWeight < n2.distanceWeight)
				return -1;
			else
				return 0;
		});

		nodePriorityQueue.add(new NodeWrapper(from, this.getFscore(from)));

		// Initialize closed list
//		Set<AbstractElenaNode> explored = new HashSet<>();

		while(!nodePriorityQueue.isEmpty()){
			AbstractElenaNode candidateNode = nodePriorityQueue.poll().wrappedNode;

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
					AbstractElenaNode targetNode = edge.getDestinationNode();

					if(this.gScores.get(targetNode) > edge.getEdgeDistance() + this.gScores.get(candidateNode)){
						this.gScores.put(targetNode, edge.getEdgeDistance() + this.gScores.get(candidateNode));
						nodePriorityQueue.add(new NodeWrapper(targetNode, this.getFscore(targetNode)));
						this.nodeAncestor.put(targetNode, candidateNode);
					}

				}
			}
		}


		return shortestPaths;
	}

	private void initializeGraph(AbstractElenaGraph graph, AbstractElenaNode origin, AbstractElenaNode destination) {

		float destinationLat = Float.parseFloat(destination.getLatitude());
		float destinationLon = Float.parseFloat(destination.getLongitude());

		// Iterate through each node in graph to initialize them
		for(AbstractElenaNode node : graph.getAllNodes()) {
			gScores.put(node, Float.MAX_VALUE);
			float nodeLat = Float.parseFloat(node.getLatitude());
			float nodeLon = Float.parseFloat(node.getLongitude());
			hScores.put(node, ElenaUtils.getDistance(nodeLat, nodeLon, destinationLat, destinationLon, Units.METRIC));
		}

		// Initialize source node
		this.nodeAncestor.put(origin, null);
	}

	private float getFscore(AbstractElenaNode node){
		return this.gScores.get(node) + this.hScores.get(node);
	}
}
