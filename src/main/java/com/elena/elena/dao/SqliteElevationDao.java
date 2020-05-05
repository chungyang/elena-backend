package com.elena.elena.dao;


import com.elena.elena.model.AbstractElenaNode;
import com.elena.elena.util.Units;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.*;


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
    public int insert(Collection<AbstractElenaNode> nodes) {

        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append("INSERT INTO elevation (ID, ELEVATION) values ");

        for(AbstractElenaNode node : nodes){
            sqlBuilder.append("(")
                    .append(getParamSqlString(2))
                    .append(")")
                    .append(",");
        }
        String sql = sqlBuilder.deleteCharAt(sqlBuilder.length() - 1).toString();

        return this.jdbcTemplate.update(sql, (ps)->{
            int index = 1;
            for(AbstractElenaNode node : nodes){
                ps.setLong(index, Long.valueOf(node.getId()));
                ps.setFloat(index + 1, node.getElevationWeight());
                index += 2;
            }
        });
    }

    @Override
    public int delete(Set<AbstractElenaNode> elevationData) {
        return 0;
    }

    @Override
    public int get(Map<Long, AbstractElenaNode> nodes, Units unit) {

        String sql = "SELECT id, elevation FROM elevation WHERE id in (" + getParamSqlString(nodes.size()) + ")";
        int totalRetrieved = 0;

        List<AbstractElenaNode> retrievedNodes = this.jdbcTemplate.query(sql,
                (ps) -> {
                    int index = 1;
                    for(long id : nodes.keySet()){
                        ps.setLong(index, id);
                        index++;
                    }
                },
                (rs, rowNum) -> {
                    AbstractElenaNode node = nodes.get(rs.getLong("id"));
                    node.setElevationWeight(rs.getFloat("elevation"));
                    return node;
                });

        //All data are available in database, no need to fetch it from external source
        if(retrievedNodes.size() == nodes.size()){
            return retrievedNodes.size();
        }

        totalRetrieved += retrievedNodes.size();
        for(AbstractElenaNode node : retrievedNodes){
            nodes.remove(node);
        }

        totalRetrieved += this.httpDao.get(nodes, unit);
        this.insert(nodes.values());

        return totalRetrieved;
    }

    @Override
    public int update(Set<AbstractElenaNode> nodes) {
        return 0;
    }


    private  String getParamSqlString(int paramSize){
        StringBuilder stringBuilder = new StringBuilder();
        for(int i = 0; i < paramSize; i++){
            stringBuilder.append("?,");
        }

        return stringBuilder.deleteCharAt(stringBuilder.length() - 1).toString();
    }


    @Override
    public void close() throws IOException {
        httpDao.close();
    }
}
