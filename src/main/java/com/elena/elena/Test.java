package com.elena.elena;

import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversal;
import org.apache.tinkerpop.gremlin.structure.Graph;
import org.apache.tinkerpop.gremlin.structure.io.IoCore;
import org.apache.tinkerpop.gremlin.tinkergraph.structure.TinkerGraph;

import java.io.File;
import java.io.IOException;
import java.net.URL;

public class Test {

    public String getFilePath(String fileName) {
        URL res = getClass().getClassLoader().getResource(fileName);
        return res.getPath();
    }

    public static void main(String[] args) throws IOException {
        Test t = new Test();
        Graph graph = TinkerGraph.open();
        graph.io(IoCore.graphml()).readGraph(t.getFilePath("network.graphml"));
        int i = 0;
    }
}