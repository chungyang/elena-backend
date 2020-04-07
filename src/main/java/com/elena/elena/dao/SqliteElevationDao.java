package com.elena.elena.dao;


import com.elena.elena.model.AbstractElenaNode;
import com.elena.elena.util.ElenaUtils;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mchange.v2.c3p0.DataSources;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Repository
public class SqliteElevationDao implements ElevationDao{


    private JdbcTemplate jdbcTemplate;
    private PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
    private ExecutorService executorService = Executors.newFixedThreadPool(5);
    private final String ELEVATION_KEY_NAME = "Elevation";
    private final String ELEVATION_SOURCE_HOST = "nationalmap.gov/epqs/pqs.php";

    @Autowired
    public void init(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        connectionManager.setMaxTotal(200);
        connectionManager.setDefaultMaxPerRoute(10);
    }

    @Override
    public boolean insert(List<ElevationData> elevationData) {
        String s = concatValues2String(elevationData);
        String sql = "INSERT INTO elevation (ID, ELEVATION) values " + s;
        int i = 0;

        return false;
    }

    @Override
    public boolean delete(List<ElevationData> elevationData) {
        return false;
    }

    @Override
    public List<ElevationData> get(List<String> ids) {
        String sql = "SELECT id, elevation FROM elevation WHERE id in (" + concatValues2String(ids) + ")";

        return this.jdbcTemplate.query(sql, (rs, rowNum) ->
                new ElevationData(rs.getString("id"),
                        Float.valueOf(rs.getString("elevation"))));
    }

    @Override
    public boolean update(List<ElevationData> elevationData) {
        return false;
    }

    private <T> String concatValues2String(List<T> values){
        StringBuilder stringBuilder = new StringBuilder();
        for(T value : values){
            stringBuilder.append(value.toString() + ",");
        }

        return stringBuilder.deleteCharAt(stringBuilder.length() - 1).toString();
    }


    private String getCoordinate(AbstractElenaNode node){

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(node.getLatitude()).append(",").append(node.getLongitude());
        return stringBuilder.toString();
    }

    private Float httpGetElevation(AbstractElenaNode node, CloseableHttpClient httpClient){

        NameValuePair lat = new BasicNameValuePair("x", node.getLongitude());
        NameValuePair lon = new BasicNameValuePair("y", node.getLatitude());
        NameValuePair units = new BasicNameValuePair("units", "feet");
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
                EntityUtils.consume(entity);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return elevation;
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
}
