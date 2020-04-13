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

import java.io.IOException;
import java.util.*;

public class ElenaGraph extends AbstractElenaGraph{

    //Three hashmaps are probably redundant, but there is an indeterminate number
    //of nodes if we allow the whole world map to be loaded. Three separate hashmaps
    //with different key format can pretty much guarantee there won't be any key collision
    //in the future.
    private Map<String, AbstractElenaNode> nodesById;
    private Map<String, AbstractElenaNode> nodesByName;
    private Map<String, AbstractElenaNode> nodesByCoordinate;
    private Map<String, AbstractElenaEdge> edges;
    private int BATCH_PROCESS_NUMBER = 20;
    private final ElevationDao elevationDao;

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

//        this.importNodes(graph);
        this.importEdges(graph);
    }

    private void importNodes(@NonNull Graph graph){

        Iterator<Vertex> vertices = graph.vertices();
        Map<ElevationData, AbstractElenaNode> data = new HashMap<>();

        while(vertices.hasNext()){

            Vertex vertex = vertices.next();
            AbstractElenaNode elenaNode = new ElenaNode(this, vertex);
            data.put(new ElevationData(elenaNode.getId(), elenaNode.getLatitude(), elenaNode.getLongitude()), elenaNode);

            if(data.size() == BATCH_PROCESS_NUMBER){
                for(ElevationData d : elevationDao.get(data.keySet())){
                    data.get(d).setElevationWeight(d.getElevation());
                }
                data.clear();
            }

            this.nodesById.put(elenaNode.getId(), elenaNode);
            this.nodesByCoordinate.put(this.getCoordinate(elenaNode), elenaNode);
        }

        //Leftover processing
        if(!data.isEmpty()) {
            for (ElevationData d : elevationDao.get(data.keySet())) {
                data.get(d).setElevationWeight(d.getElevation());
            }
        }
    }

    private void importEdges(@NonNull Graph graph){

        Iterator<Edge> tinkerEdges = graph.edges();

        while(tinkerEdges.hasNext()){

            Edge edge = tinkerEdges.next();
            AbstractElenaEdge elenaEdge = new ElenaEdge(this, edge);
            edges.put(elenaEdge.getId(), elenaEdge);
            ElenaPath elenaPath = new ElenaPath();
            elenaPath.addEdgeToPath(0, elenaEdge);
            elenaPath.toString();
            if(edge.property("name").isPresent()){
                String[] parsedNames = this.parseLocationNames(edge.property("name").value().toString().toLowerCase());
                for(String name : parsedNames) {
                    this.nodesByName.putIfAbsent(name, elenaEdge.getOriginNode());
                }
            }
        }
    }

    /**
     * Some edge has different names that has a format [name1, name2...]. For example, ['arden road', 'arden path']
     */
    private String[] parseLocationNames(String names){

        String specialChar = "[\\[\\]']";
        names = names.replaceAll(specialChar, "");

        return names.split(", ");
    }

    private String getCoordinate(AbstractElenaNode node){

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(node.getLatitude()).append(",").append(node.getLongitude());
        return stringBuilder.toString();
    }

    @Override
    public Collection<AbstractElenaNode> getAllNodes() {
        return nodesById.values();
    }

    @Override
    public Collection<AbstractElenaEdge> getAllEdges() {
        return this.edges.values();
    }

    @Override
    public Collection<String> getLocationNames() {
        return this.nodesByName.keySet();
    }

    @Override
    public Optional<AbstractElenaNode> getNode(String id) {

        id = id.toLowerCase();
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