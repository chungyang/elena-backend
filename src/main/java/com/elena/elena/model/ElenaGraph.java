package com.elena.elena.model;

import com.elena.elena.dao.ElevationDao;
import com.elena.elena.dao.ElevationData;
import com.elena.elena.util.ElenaUtils;
import lombok.NonNull;
import org.apache.tinkerpop.gremlin.structure.Edge;
import org.apache.tinkerpop.gremlin.structure.Graph;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.apache.tinkerpop.gremlin.structure.io.IoCore;
import org.apache.tinkerpop.gremlin.tinkergraph.structure.TinkerGraph;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.*;

@Component
public class ElenaGraph extends AbstractElenaGraph{

    //Three hashmaps are probably redundant, but there is an indeterminate number
    //of nodes if we allow the whole world map to be loaded. Three separate hashmaps
    //with different key format can pretty much guarantee there won't be any key collision
    //in the future.
    private Map<String, AbstractElenaNode> nodesById;
    private Map<String, AbstractElenaNode> nodesByName;
    private Map<String, AbstractElenaNode> nodesByCoordinate;
    private Map<String, AbstractElenaEdge> edges;
    private ElevationDao elevationDao;


    public ElenaGraph(@NonNull String graphmlFileName, ElevationDao elevationDao) throws IOException {

        Graph graph = TinkerGraph.open();
        graph.io(IoCore.graphml()).readGraph(ElenaUtils.getFilePath(graphmlFileName));
        this.nodesById = new HashMap<>();
        this.nodesByName = new HashMap<>();
        this.nodesByCoordinate = new HashMap<>();
        this.edges = new HashMap<>();
        this.elevationDao = elevationDao;
        this.importGraph(graph);
    }


    /**
     * This method should be used to import the whole graph. {@link #importNodes(Graph)}
     * and {@link #importEdges(Graph)} should not be called by itself because the order
     * of the calls matter.
     * @param graph
     */
    private void importGraph(@NonNull Graph graph){

        this.importNodes(graph);
        this.importEdges(graph);
    }


    private void importNodes(@NonNull Graph graph){

        Iterator<Vertex> vertices = graph.vertices();

        while(vertices.hasNext()){

            Vertex vertex = vertices.next();
            AbstractElenaNode elenaNode = new ElenaNode(this, vertex);
            Set<ElevationData> data = new HashSet<>();
            data.add(new ElevationData(elenaNode.getId(), elenaNode.getLatitude(), elenaNode.getLongitude()));

            for(ElevationData d : elevationDao.get(data)){
                elenaNode.setElevationWeight(d.getElevation());
            }
            this.nodesById.put(elenaNode.getId(), elenaNode);
            this.nodesByCoordinate.put(this.getCoordinate(elenaNode), elenaNode);
        }
    }

    private void importEdges(@NonNull Graph graph){

        Iterator<Edge> tinkerEdges = graph.edges();

        while(tinkerEdges.hasNext()){

            Edge edge = tinkerEdges.next();
            AbstractElenaEdge elenaEdge = new ElenaEdge(this, edge);
            edges.put(elenaEdge.getId(), elenaEdge);

            if(edge.property("name").isPresent()){
                this.nodesByName.putIfAbsent(edge.property("name").value().toString(), elenaEdge.getOriginNode());
            }
        }
    }

    private String getCoordinate(AbstractElenaNode node){

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(node.getLatitude()).append(",").append(node.getLongitude());
        return stringBuilder.toString();
    }


    @Override
    public Optional<AbstractElenaNode> getNode(String id) {

        AbstractElenaNode node;

        if(this.nodesById.containsKey(id)){
            node =  this.nodesById.get(id);
        }
        else if(this.nodesByCoordinate.containsKey(id)){
            node = this.nodesByCoordinate.get(id);
        }
        else{
            node = this.nodesByName.getOrDefault(id, null);
        }

        Optional<AbstractElenaNode> optional = Optional.ofNullable(node);
        return optional;
    }

    @Override
    public AbstractElenaEdge getEdge(String id) {
        return this.edges.get(id);
    }

    @Override
    public void cleanup() {
//        this.executorService.shutdown();
    }
}
