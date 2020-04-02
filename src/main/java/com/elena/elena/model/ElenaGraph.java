package com.elena.elena.model;

import com.elena.elena.util.ElenaUtils;
import org.apache.tinkerpop.gremlin.structure.Edge;
import org.apache.tinkerpop.gremlin.structure.Graph;
import org.apache.tinkerpop.gremlin.structure.io.IoCore;
import org.apache.tinkerpop.gremlin.tinkergraph.structure.TinkerGraph;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ElenaGraph extends AbstractElenaGraph {

    public ElenaGraph(String graphmlFileName) throws IOException {
        Graph graph = TinkerGraph.open();
        graph.io(IoCore.graphml()).readGraph(ElenaUtils.getFilePath(graphmlFileName));
    }

    private List<AbstractElenaEdge> mapEdges(org.apache.tinkerpop.gremlin.structure.Graph graph){

        List<AbstractElenaEdge> elenaEdges = new ArrayList<>();
        while(graph.edges().hasNext()){
            Edge edge = graph.edges().next();

        }

        return elenaEdges;
    }

    private AbstractElenaEdge mapEdge(Edge edge){
        AbstractElenaEdge elenaEdge = new ElenaEdge();

        return elenaEdge;
    }

    public static void main(String[] args) throws IOException {
//        Test t = new Test();

//        int i = 0;
    }


}
