package com.elena.elena.model;

import com.elena.elena.dao.ElevationDao;
import com.elena.elena.util.ElenaUtils;
import com.elena.elena.util.Units;
import lombok.NonNull;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.tinkerpop.gremlin.structure.*;
import org.apache.tinkerpop.gremlin.structure.io.graphml.GraphMLReader;
import org.apache.tinkerpop.gremlin.tinkergraph.structure.TinkerGraph;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
@Qualifier("sqliteDao")
public class ElenaGraph extends AbstractElenaGraph{

    //Three hashmaps are probably redundant, but there is an indeterminate number
    //of nodes if we allow the whole world map to be loaded. Three separate hashmaps
    //with different key format can pretty much guarantee there won't be any key collision
    //in the future.
    private Map<String, AbstractElenaNode> nodesById;
    private Map<String, AbstractElenaNode> nodesByName;
    private Map<Coordinate, AbstractElenaNode> nodesByCoordinate;
    private Map<String, AbstractElenaEdge> edges;
    private final ElevationDao elevationDao;
    private ExecutorService executorService = Executors.newFixedThreadPool(20);



    public ElenaGraph(@NonNull String graphmlFileName, @NonNull  @Qualifier("sqliteDao") ElevationDao elevationDao) throws IOException {

        Graph graph = TinkerGraph.open();
        GraphMLReader reader = GraphMLReader.build().create();
        reader.readGraph(ElenaUtils.getFileAsInputStream(graphmlFileName), graph);
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
    private void importGraph(@NonNull Graph graph) {

        this.importNodes(graph);
        this.importEdges(graph);
        //Because a node lazily initializes its outgoing edges, the computation
        //is done in downstream tasks and thus cause them to be slower. Doing
        //initialization here improve performance.
        for(AbstractElenaNode node : nodesById.values()){
            node.getOutGoingEdges();
        }
    }

    private void importNodes(@NonNull Graph graph){

        Iterator<Vertex> vertices = graph.vertices();
        Map<Integer, Collection<AbstractElenaNode>> batchProcessMap = new HashMap<>();
        int currentBatch = 1;
        Units unit = Units.METRIC;

        while(vertices.hasNext()){

            Vertex vertex = vertices.next();
            AbstractElenaNode elenaNode = new ElenaNode(this, vertex);
            batchProcessMap.putIfAbsent(currentBatch, new ArrayList<>());
            batchProcessMap.get(currentBatch).add(elenaNode);

            int batchNumber = 100;
            if(batchProcessMap.get(currentBatch).size() == batchNumber){

                final int executingBatchNumber = currentBatch;
                this.executorService.submit(()->{
                    int retrievedNumber = elevationDao.get(batchProcessMap.get(executingBatchNumber), unit);
                    if(retrievedNumber != batchNumber){
                        throw new IllegalStateException("Some elevation data were not retrieved");
                    }
                    batchProcessMap.remove(executingBatchNumber);
                });
                currentBatch++;
            }


            this.nodesById.put(elenaNode.getId(), elenaNode);
            this.nodesByCoordinate.put(
                    new Coordinate(Float.parseFloat(elenaNode.getLatitude()), Float.parseFloat(elenaNode.getLongitude())),
                    elenaNode);
        }

        //Leftover processing
        if(!batchProcessMap.get(currentBatch).isEmpty()) {
            int retrievedNumber = elevationDao.get(batchProcessMap.get(currentBatch), unit);
            if(retrievedNumber != batchProcessMap.get(currentBatch).size()){
                throw new IllegalStateException("Some elevation data were not retrieved");
            }
        }
        executorService.shutdown();
    }

    private void importEdges(@NonNull Graph graph){

        Iterator<Edge> tinkerEdges = graph.edges();
        while(tinkerEdges.hasNext()){

            Edge edge = tinkerEdges.next();
            AbstractElenaEdge elenaEdge = new ElenaEdge(this, edge);
            edges.put(elenaEdge.getId(), elenaEdge);

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
    public Optional<AbstractElenaNode> getNodeByID(String id) {
        return Optional.ofNullable(this.nodesById.getOrDefault(id, null));
    }

    @Override
    public Optional<AbstractElenaNode> getNodeByAddress(String address) {
        return Optional.ofNullable(this.nodesByName.getOrDefault(address.toLowerCase(), null));
    }

    @Override
    public Optional<AbstractElenaNode> getNodeByCoordinate(String latitude, String longitutde) {
        Coordinate coordinate = new Coordinate(Float.parseFloat(latitude), Float.parseFloat(longitutde));
        return Optional.ofNullable(this.nodesByCoordinate.getOrDefault(coordinate, null));
    }

    @Override
    public AbstractElenaEdge getEdge(String id) {
        return this.edges.get(id);
    }

    @Override
    public void cleanup() {
        try {
            this.elevationDao.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private static class ElenaEdge extends AbstractElenaEdge{
        private final Edge tinkerEdge;
        private final AbstractElenaGraph graph;
        private  Map<String, String> properties;
        private float edgeDistance;

        private final String LENGTH__PROPERTY_KEY = "length";

        public  ElenaEdge(AbstractElenaGraph graph, Edge tinkerEdge){
            this.tinkerEdge = tinkerEdge;
            this.graph = graph;
            properties = new HashMap<>();
            this.importProperties();
            this.edgeDistance = Float.parseFloat((String) tinkerEdge.property(LENGTH__PROPERTY_KEY).value());
        }

        private void importProperties(){

            Iterator<Property<String>> properties = tinkerEdge.properties();

            while(properties.hasNext()){
                Property<String> property = properties.next();
                String key = property.key();
                String value = property.value();
                this.properties.put(key, value);
            }
        }

        @Override
        public String getId() {
            return (String) tinkerEdge.id();
        }

        @Override
        public float getEdgeDistance() {
            return this.edgeDistance;
        }

        @Override
        public void setEdgeDistance(float distance) {
            this.edgeDistance = distance;
        }

        @Override
        public float getEdgeElevation() {
            return Math.max(0, this.getDestinationNode().getElevationWeight() - this.getOriginNode().getElevationWeight());
        }

        @Override
        public AbstractElenaNode getOriginNode() {
            String originNodeId = (String) tinkerEdge.outVertex().id();
            return this.graph.getNodeByID(originNodeId).get();
        }

        @Override
        public AbstractElenaNode getDestinationNode() {
            String destinationNodeId = (String) tinkerEdge.inVertex().id();
            return this.graph.getNodeByID(destinationNodeId).get();
        }

        @Override
        public Map<String, String> getProperties() {
            return this.properties;
        }

        @Override
        public Collection<Coordinate> getCoordinates() {
            String[] coordinateString = this.getProperties().getOrDefault("geometry", "")
                    .substring("LINESTRING ".length()).replaceAll("[(),]", "").split(" ");
            List<Coordinate> coordinates = new ArrayList<>();

            if(coordinateString.length % 2 != 0){
                throw new IllegalStateException("Parsed coordinates should be divisible by 2");
            }

            //Leaflet accepts (lat,lon) pairs instead of (lon,lat) so we need to change the order here
            //since openstreetmap stores geometry in (lon,lat)
            for(int i = 0; i < coordinateString.length; i+=2){
                coordinates.add(new Coordinate(Float.parseFloat(coordinateString[i+1]), Float.parseFloat(coordinateString[i])));
            }

            return coordinates;
        }

    }

    private static class ElenaNode extends AbstractElenaNode{

        private final Vertex tinkerVertex;
        private final AbstractElenaGraph graph;
        private Map<AbstractElenaNode, AbstractElenaEdge> outgoingEdges = new HashMap();
        private List<AbstractElenaEdge> incomingEdges = new ArrayList<>();
        private List<AbstractElenaNode> neighbors = new ArrayList<>();

        public ElenaNode(AbstractElenaGraph graph, Vertex tinkerVertex){

            this.graph = graph;
            this.tinkerVertex = tinkerVertex;
        }

        @Override
        public String getId() {
            return (String) tinkerVertex.id();
        }


        @Override
        public Float getElevationWeight() {
            return this.elevationWeight;
        }

        @Override
        public Collection<AbstractElenaNode> getNeighbors() {

            if(neighbors.isEmpty()) {
                for (AbstractElenaEdge edge : this.getOutGoingEdges()) {
                    neighbors.add(edge.getDestinationNode());
                }
            }
            return neighbors;
        }

        @Override
        public Collection<AbstractElenaEdge> getOutGoingEdges() {

            if(outgoingEdges.isEmpty()) {
                tinkerVertex.edges(Direction.OUT).forEachRemaining(edge -> {
                    outgoingEdges.put(this.graph.getNodeByID((String) edge.inVertex().id()).get(),
                            this.graph.getEdge((String) edge.id()));
                });
            }
            return outgoingEdges.values();
        }

        @Override
        public Collection<AbstractElenaEdge> getInComingEdges() {

            if(incomingEdges.isEmpty()) {
                tinkerVertex.edges(Direction.IN).forEachRemaining(edge -> {
                    incomingEdges.add(this.graph.getEdge((String) edge.id()));
                });
            }
            return incomingEdges;
        }

        @Override
        public String getLatitude() {
            return this.tinkerVertex.property("y").value().toString();
        }

        @Override
        public String getLongitude() {
            return this.tinkerVertex.property("x").value().toString();
        }

        @Override
        public Optional<AbstractElenaEdge> getEdge(AbstractElenaNode destinationNode) {
            return Optional.ofNullable(outgoingEdges.getOrDefault(destinationNode, null));
        }

        @Override
        public boolean equals(Object o) {

            if(!(o instanceof AbstractElenaNode)){
                return false;
            }

            if(o == this){
                return true;
            }
            ElenaNode node = (ElenaNode) o;

            return node.getId().equals(this.getId());
        }

        @Override
        public int hashCode(){
            return this.getId().hashCode();
        }

    }
}