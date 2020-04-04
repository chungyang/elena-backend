package com.elena.elena.model;

import com.elena.elena.util.ElenaUtils;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import lombok.Cleanup;
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
import org.apache.tinkerpop.gremlin.process.traversal.P;
import org.apache.tinkerpop.gremlin.structure.Direction;
import org.apache.tinkerpop.gremlin.structure.Vertex;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ElenaNode extends AbstractElenaNode {

    private final Vertex tinkerVertex;
    private final AbstractElenaGraph graph;
    private final String ELEVATION_SOURCE_HOST = "nationalmap.gov/epqs/pqs.php";
    private static PoolingHttpClientConnectionManager connectionManager;


    public ElenaNode(AbstractElenaGraph graph, Vertex tinkerVertex){

        this.graph = graph;
        this.tinkerVertex = tinkerVertex;
        this.connectionManager = new PoolingHttpClientConnectionManager();
        this.connectionManager.setMaxTotal(20);
        this.connectionManager.setDefaultMaxPerRoute(20);
    }

    @Override
    public String getId() {
        return (String) tinkerVertex.id();
    }

    @Override
    public Float getDistanceWeight() {
        return this.distanceWeight;
    }

    @Override
    public Float getElevationWeight() {

        return this.elevationWeight == null? this.elevationWeight = httpGetElevation():
                this.elevationWeight;
    }

    private Float httpGetElevation(){

        NameValuePair lat = new BasicNameValuePair("x", this.getLongitude());
        NameValuePair lon = new BasicNameValuePair("y", this.getLatitude());
        NameValuePair units = new BasicNameValuePair("units", "feet");
        NameValuePair output = new BasicNameValuePair("output", "json");
        Optional<URI> optionalURI = ElenaUtils.getURL(ELEVATION_SOURCE_HOST, "", "http",
                lat, lon, units, output);
        Float elevation = null;

        if(optionalURI.isPresent()){

            final HttpGet httpGet = new HttpGet(optionalURI.get());
            try(CloseableHttpClient httpClient = HttpClients.custom()
                    .setConnectionManager(this.connectionManager).build();
                CloseableHttpResponse response = httpClient.execute(httpGet)){

                StatusLine statusLine = response.getStatusLine();
                HttpEntity entity = response.getEntity();

                if(statusLine.getStatusCode() != 200){
                    throw new HttpResponseException(statusLine.getStatusCode(), statusLine.getReasonPhrase());
                }

                if(entity == null){
                    throw new ClientProtocolException("Response contains no content");
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return elevation;
    }

    @Override
    public List<AbstractElenaNode> getNeighbors() {

        List<AbstractElenaNode> neighbors = new ArrayList<>();

        for(AbstractElenaEdge edge : this.getOutGoingEdges()){
            neighbors.add(edge.getDestinationNode());
        }
        return neighbors;
    }

    @Override
    public List<AbstractElenaEdge> getOutGoingEdges() {

        List<AbstractElenaEdge> outgoingEdges = new ArrayList<>();
        tinkerVertex.edges(Direction.OUT).forEachRemaining(edge -> {
            outgoingEdges.add(this.graph.getEdge((String) edge.id()));
        });
        return outgoingEdges;
    }

    @Override
    public List<AbstractElenaEdge> getIncomingEdges() {
        List<AbstractElenaEdge> outgoingEdges = new ArrayList<>();
        tinkerVertex.edges(Direction.IN).forEachRemaining(edge -> {
            outgoingEdges.add(this.graph.getEdge((String) edge.id()));
        });
        return outgoingEdges;
    }

    @Override
    public String getLatitude() {
        return this.tinkerVertex.property("lat").value().toString();
    }

    @Override
    public String getLongitude() {
        return this.tinkerVertex.property("lon").value().toString();
    }

}
