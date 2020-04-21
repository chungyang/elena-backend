package com.elena.elena.routing;

import com.elena.elena.model.AbstractElenaGraph;
import com.elena.elena.model.AbstractElenaNode;
import com.elena.elena.model.AbstractElenaEdge;
import com.elena.elena.model.AbstractElenaPath;
import com.elena.elena.model.ElenaPath;

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
	private Map<AbstractElenaNode, Float> node_Gscore; // Distance from source
	private Map<AbstractElenaNode, Float> node_Hscore; // Distance from destination
	private Map<AbstractElenaNode, Float> nodeTentativeDistance;
	public static final double R = 6372.8; // In kilometers (for H_score Calculation)
	
	// Constructor
	public AstarRouter() {
		this.nodeAncestor = new HashMap<>();
		this.node_Gscore = new HashMap<>();
		this.node_Hscore = new HashMap<>();
		this.nodeTentativeDistance = new HashMap<>();
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
		this.initializeGraph(graph, from);
		
		// Initialize source node
		// Calculate h (distance from destination)
		float source_h_score = this.calculate_Hscore(from, to);
		this.node_Hscore.put(from, source_h_score);
		// f = g (distance from source) + h (distance from destination)
		this.node_Gscore.put(from, 0f);
		float source_f_score = this.node_Gscore.get(from) + this.node_Hscore.get(from);
		nodeTentativeDistance.put(from, source_f_score);
		
		// Initialize open list
		PriorityQueue<NodeWrapper> nodePriorityQueue = new PriorityQueue<>((n1 , n2) -> {
			if(n1.distanceWeight > n2.distanceWeight)
				return 1;
			else if(n1.distanceWeight < n2.distanceWeight)
				return -1;
			else
				return 0;
		});
		
		nodePriorityQueue.add(new NodeWrapper(from, nodeTentativeDistance.get(from)));

		// Initialize closed list
		Set<AbstractElenaNode> explored = new HashSet<AbstractElenaNode>();
		
		while(!nodePriorityQueue.isEmpty()){
			AbstractElenaNode candidateNode = nodePriorityQueue.poll().wrappedNode;
			explored.add(candidateNode);
			
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
					NodeWrapper wrappedOutNode = new NodeWrapper(targetNode, nodeTentativeDistance.get(targetNode));
					
					if((!explored.contains(targetNode)) && (!nodePriorityQueue.contains(wrappedOutNode))) {
							
						nodePriorityQueue.add(wrappedOutNode);
						
						// g_score
						float temp_g_score = this.node_Gscore.get(candidateNode) + edge.getEdgeDistance();					
						
						// h_score
						if(!this.node_Hscore.containsKey(targetNode)) {
							float temp_h_score = this.calculate_Hscore(targetNode, to);
							this.node_Hscore.put(targetNode, temp_h_score);
						}
				
						// f_score
						float temp_f_score = temp_g_score + this.node_Hscore.get(targetNode);
						
						if (temp_f_score < nodeTentativeDistance.get(targetNode)) {
                    		this.node_Gscore.put(targetNode, temp_g_score);
                    		nodeTentativeDistance.put(targetNode, temp_f_score);    
                    		this.nodeAncestor.put(targetNode, candidateNode);
						}
					}              			        
				}
			}			
		}
		
		return shortestPaths;
    }
    
	private void initializeGraph(AbstractElenaGraph graph, AbstractElenaNode from) {

		// Iterate through each node in graph to initialize them
		for(AbstractElenaNode node : graph.getAllNodes()) {
			nodeTentativeDistance.put(node, Float.MAX_VALUE);
		}

		// Initialize source node
		this.nodeAncestor.put(from, null);
	}
	
	// Calculate distance from destination (H_score) using Haversine formula
	public float calculate_Hscore(AbstractElenaNode target, AbstractElenaNode dest) {	    
		double source_lat = Float.parseFloat(target.getLatitude());
		double source_lon = Float.parseFloat(target.getLongitude());
		double dest_lat = Float.parseFloat(dest.getLatitude());
		double dest_lon = Float.parseFloat(dest.getLongitude());	
		
		double dist_lat = Math.toRadians(dest_lat - source_lat);
	    double dist_lon = Math.toRadians(dest_lon - source_lon);
	    source_lat = Math.toRadians(source_lat);
	    dest_lat = Math.toRadians(dest_lat);
	 
	    double a = Math.pow(Math.sin(dist_lat / 2),2) + Math.pow(Math.sin(dist_lon / 2),2) * Math.cos(source_lat) * Math.cos(dest_lat);
	    double c = 2 * Math.asin(Math.sqrt(a));
	    double h_score = R * c;
	    return (float)h_score;
	}
}
