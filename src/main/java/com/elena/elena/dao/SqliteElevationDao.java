package com.elena.elena.dao;


import com.elena.elena.model.AbstractElenaNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Repository("sqliteDao")
public class SqliteElevationDao implements ElevationDao{

    private JdbcTemplate jdbcTemplate;

    @Autowired
    @Qualifier("httpDao")
    private ElevationDao httpDao;

    @Autowired
    public void init(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public int insert(Set<ElevationData> elevationData) {
        String s = concatValues2String(elevationData);
        String sql = "INSERT INTO elevation (ID, ELEVATION) values " + s;
        return this.jdbcTemplate.update(sql);
    }

    @Override
    public int delete(Set<ElevationData> elevationData) {
        return 0;
    }

    @Override
    public Collection<ElevationData> get(Set<ElevationData> elevationData) {

        Set<String> ids = new HashSet<>();
        for(ElevationData data : elevationData){
            ids.add(data.getId());
        }

        String sql = "SELECT id, elevation FROM elevation WHERE id in (" + concatValues2String(ids) + ")";

        List<ElevationData> retrievedData = this.jdbcTemplate.query(sql, (rs, rowNum) ->
                new ElevationData(rs.getString("id"),
                        Float.valueOf(rs.getString("elevation"))));

        //All data are available in database, no need to fetch it from external source
        if(retrievedData.size() == elevationData.size()){
            return retrievedData;
        }

        for(ElevationData data : retrievedData){
            elevationData.remove(data);
        }

        Set<ElevationData> httpRetrievedData = new HashSet<>();
        httpRetrievedData.addAll(this.httpDao.get(elevationData));
        this.insert(httpRetrievedData);
        retrievedData.addAll(httpRetrievedData);

        return retrievedData;
    }

    @Override
    public int update(Set<ElevationData> elevationData) {
        return 0;
    }


    private <T> String concatValues2String(Iterable<T> values){
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


    @Override
    public void close(){
        //The implementation is empty because JdbcTemplate takes care of the clean
        //up
    }
}
