package com.elena.elena.dao;

import com.elena.elena.model.AbstractElenaNode;
import com.elena.elena.util.ElenaUtils;
import com.elena.elena.util.Units;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * This implementation targets a third party host that allows operations
 * that's read-only, only {@link #get(Collection, Units)} is implemented.
 */
@Component("httpDao")
public class HttpDao implements ElevationDao{

    private PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
    @Value("${usgs.elevation.key}") private String ELEVATION_KEY_NAME;
    @Value("${usgs.elevation.host}") private String ELEVATION_SOURCE_HOST;
    private CloseableHttpClient httpClient = HttpClients.custom()
            .setConnectionManager(this.connectionManager).setConnectionManagerShared(true).build();
    private ExecutorService executorService = Executors.newFixedThreadPool(20);

    public HttpDao(){
        connectionManager.setMaxTotal(200);
        connectionManager.setDefaultMaxPerRoute(20);
    }


    @Override
    public int insert(Collection<? extends AbstractElenaNode> nodes) {
        return 0;
    }

    @Override
    public int delete(Set<? extends AbstractElenaNode> nodes) {
        return 0;
    }

    @Override
    public int get(Collection<? extends AbstractElenaNode> nodes, Units unit) {

        List<Callable<Boolean>> tasks = new ArrayList<>();
        AtomicInteger retrievedNumber = new AtomicInteger(0);
        for(AbstractElenaNode node : nodes){

            tasks.add(()->
            {
                if(httpGetElevation(node, httpClient, unit)){
                    retrievedNumber.incrementAndGet();
                }
                return true;
            });
        }

        try {
            executorService.invokeAll(tasks);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return retrievedNumber.get();
    }

    @Override
    public int update(Collection<? extends AbstractElenaNode> elevationData) {
        return 0;
    }

    private boolean httpGetElevation(AbstractElenaNode node, CloseableHttpClient httpClient, Units unit){


        NameValuePair lat = new BasicNameValuePair("x", node.getLongitude());
        NameValuePair lon = new BasicNameValuePair("y", node.getLatitude());
        NameValuePair units = new BasicNameValuePair("units", unit.getUnit());
        NameValuePair output = new BasicNameValuePair("output", "json");
        Optional<URI> optionalURI = ElenaUtils.getURL(ELEVATION_SOURCE_HOST, "", "http",
                lat, lon, units, output);
        Float elevation = null;

        if(optionalURI.isPresent()){

            final HttpGet httpGet = new HttpGet(optionalURI.get());
            try(CloseableHttpResponse response = httpClient.execute(httpGet)){

                StatusLine statusLine = response.getStatusLine();
                HttpEntity entity = response.getEntity();

                if(statusLine.getStatusCode() != 200){
                    throw new HttpResponseException(statusLine.getStatusCode(), statusLine.getReasonPhrase());
                }

                if(entity == null){
                    throw new ClientProtocolException("Response contains no content");
                }
                elevation = parseJsonToElevation(entity.getContent());
                node.setElevationWeight(elevation);
                EntityUtils.consume(entity);

                return true;

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return false;
    }


    private Float parseJsonToElevation(InputStream jsonInput){

        ObjectMapper mapper = new ObjectMapper();
        Float parsedElevation = null;
        try {
            JsonNode jsonNode = mapper.readTree(jsonInput).findParent(ELEVATION_KEY_NAME).get(ELEVATION_KEY_NAME);
            parsedElevation = Float.valueOf(jsonNode.toString());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return parsedElevation;
    }

    @Override
    public void close() throws IOException {
        connectionManager.close();
        httpClient.close();
    }
}

