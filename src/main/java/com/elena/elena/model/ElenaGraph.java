package com.elena.elena.model;

import com.elena.elena.dao.ElevationDao;
import com.elena.elena.dao.SqliteElevationDao;
import com.elena.elena.util.ElenaUtils;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NonNull;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.tinkerpop.gremlin.structure.Edge;
import org.apache.tinkerpop.gremlin.structure.Graph;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.apache.tinkerpop.gremlin.structure.io.IoCore;
import org.apache.tinkerpop.gremlin.tinkergraph.structure.TinkerGraph;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
//    private PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
//    private ExecutorService executorService = Executors.newFixedThreadPool(5);
//    private final String ELEVATION_KEY_NAME = "Elevation";
//    private final String ELEVATION_SOURCE_HOST = "nationalmap.gov/epqs/pqs.php";
    private ElevationDao elevationDao;


    public ElenaGraph(@NonNull String graphmlFileName, ElevationDao elevationDao) throws IOException {

        Graph graph = TinkerGraph.open();
        graph.io(IoCore.graphml()).readGraph(ElenaUtils.getFilePath(graphmlFileName));
        this.nodesById = new HashMap<>();
        this.nodesByName = new HashMap<>();
        this.nodesByCoordinate = new HashMap<>();
        this.edges = new HashMap<>();
//        connectionManager.setMaxTotal(200);
//        connectionManager.setDefaultMaxPerRoute(10);
        this.elevationDao = elevationDao;
//        this.importGraph(graph);
    }


    /**
     * This method should be used to import the whole graph. {@link #importNodes(Graph)}
     * and {@link #importEdges(Graph)} should not be called by itself because the order
     * of the calls matter.
     * @param graph
     */
    private void importGraph(@NonNull Graph graph){
        
//        try(CloseableHttpClient httpClient = HttpClients.custom()
//                .setConnectionManager(this.connectionManager).setConnectionManagerShared(true).build()) {

            this.importNodes(graph);
            
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        this.importEdges(graph);
    }


    private void importNodes(@NonNull Graph graph){

        Iterator<Vertex> vertices = graph.vertices();
//        List<Callable<Float>> tasks = new ArrayList<>();

        while(vertices.hasNext()){

            Vertex vertex = vertices.next();
            AbstractElenaNode elenaNode = new ElenaNode(this, vertex);
//            tasks.add(()-> elenaNode.elevationWeight = this.httpGetElevation(elenaNode, httpClient));
            this.nodesById.put(elenaNode.getId(), elenaNode);
            this.nodesByCoordinate.put(this.getCoordinate(elenaNode), elenaNode);
        }

//        try {
//            this.executorService.invokeAll(tasks);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
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

//    private Float httpGetElevation(AbstractElenaNode node, CloseableHttpClient httpClient){
//
//        NameValuePair lat = new BasicNameValuePair("x", node.getLongitude());
//        NameValuePair lon = new BasicNameValuePair("y", node.getLatitude());
//        NameValuePair units = new BasicNameValuePair("units", "feet");
//        NameValuePair output = new BasicNameValuePair("output", "json");
//        Optional<URI> optionalURI = ElenaUtils.getURL(ELEVATION_SOURCE_HOST, "", "http",
//                lat, lon, units, output);
//        Float elevation = null;
//
//        if(optionalURI.isPresent()){
//
//            final HttpGet httpGet = new HttpGet(optionalURI.get());
//            try(CloseableHttpResponse response = httpClient.execute(httpGet)){
//
//                StatusLine statusLine = response.getStatusLine();
//                HttpEntity entity = response.getEntity();
//
//                if(statusLine.getStatusCode() != 200){
//                    throw new HttpResponseException(statusLine.getStatusCode(), statusLine.getReasonPhrase());
//                }
//
//                if(entity == null){
//                    throw new ClientProtocolException("Response contains no content");
//                }
//                elevation = parseJsonToElevation(entity.getContent());
//                EntityUtils.consume(entity);
//
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//        return elevation;
//    }
//
//
//    private Float parseJsonToElevation(InputStream jsonInput){
//
//        ObjectMapper mapper = new ObjectMapper();
//        Float parsedElevation = null;
//        try {
//            JsonNode jsonNode = mapper.readTree(jsonInput).findParent(ELEVATION_KEY_NAME).get(ELEVATION_KEY_NAME);
//            parsedElevation = Float.valueOf(jsonNode.toString());
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return parsedElevation;
//    }

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
