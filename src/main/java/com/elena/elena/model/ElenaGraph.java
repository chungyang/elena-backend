package com.elena.elena.model;

import com.elena.elena.util.ElenaUtils;
import lombok.NonNull;
import org.apache.tinkerpop.gremlin.structure.Edge;
import org.apache.tinkerpop.gremlin.structure.Graph;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.apache.tinkerpop.gremlin.structure.io.IoCore;
import org.apache.tinkerpop.gremlin.tinkergraph.structure.TinkerGraph;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;

public class ElenaGraph extends AbstractElenaGraph{

    private Map<String, AbstractElenaNode> nodesIdByTinkerPopId;
    private Map<String, AbstractElenaNode> nodesIdByName;
    private Map<String, AbstractElenaNode> nodesIdByCoordinate;
    private Map<String, AbstractElenaEdge> edges;

    public ElenaGraph(@NonNull String graphmlFileName) throws IOException {
        Graph graph = TinkerGraph.open();
        graph.io(IoCore.graphml()).readGraph(ElenaUtils.getFilePath(graphmlFileName));
        this.nodesIdByTinkerPopId = new HashMap<>();
        this.nodesIdByName = new HashMap<>();
        this.nodesIdByCoordinate = new HashMap<>();
        this.edges = new HashMap<>();
        this.importNodes(graph);
        this.importEdges(graph);
    }

    private void importNodes(@NonNull Graph graph){

        Iterator<Vertex> vertices = graph.vertices();
        while(vertices.hasNext()){
            Vertex vertex = vertices.next();
            AbstractElenaNode elenaNode = new ElenaNode(this, vertex);
            this.nodesIdByTinkerPopId.put(elenaNode.getId(), elenaNode);
            this.nodesIdByCoordinate.put(this.getCoordinate(vertex), elenaNode);
        }
    }

    private void importEdges(@NonNull Graph graph){

        Iterator<Edge> tinkerEdges = graph.edges();
        while(tinkerEdges.hasNext()){
            AbstractElenaEdge elenaEdge = new ElenaEdge(this, tinkerEdges.next());
            edges.put(elenaEdge.getId(), elenaEdge);
        }
    }

    private String getCoordinate(Vertex vertex){

        String latitude = (String) vertex.property("lat").value();
        String longitude = (String) vertex.property("lon").value();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(latitude).append(" ").append(longitude);
        return stringBuilder.toString();
    }


    @Override
    public Optional<AbstractElenaNode> getNode(String id) {

        AbstractElenaNode node;

        if(this.nodesIdByTinkerPopId.containsKey(id)){
            node =  this.nodesIdByTinkerPopId.get(id);
        }
        else if(this.nodesIdByCoordinate.containsKey(id)){
            node = this.nodesIdByCoordinate.get(id);
        }
        else{
            node = this.nodesIdByName.getOrDefault(id, null);
        }

        Optional<AbstractElenaNode> optional = Optional.ofNullable(node);
        return optional;
    }

    @Override
    public AbstractElenaEdge getEdge(String id) {
        return this.edges.get(id);
    }
}
