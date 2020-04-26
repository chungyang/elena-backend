package com.elena.elena;

import com.elena.elena.autocomplete.AutoCompleter;
import com.elena.elena.autocomplete.TrieAutoCompleter;
import com.elena.elena.dao.ElevationDao;
import com.elena.elena.dao.ElevationData;
import com.elena.elena.model.AbstractElenaGraph;
import com.elena.elena.model.ElenaGraph;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;

@Configuration
public class TestConfiguration {


    @MockBean
    private ElevationDao mockDao;


    @Bean("simple.graphml")
    public AbstractElenaGraph simpleGraph() throws IOException {

        Mockito.doReturn(new ArrayList<ElevationData>()).when(mockDao).get(any(Set.class));
        return new ElenaGraph("simple.graphml", mockDao);
    }

    @Bean("simple.elevation.graphml")
    public AbstractElenaGraph simpleElevationGraph() throws IOException {

        List<ElevationData> elevationData = new ArrayList<>();
        elevationData.add(new ElevationData("n0", 6));
        elevationData.add(new ElevationData("n1", 4));
        elevationData.add(new ElevationData("n2", 6));
        elevationData.add(new ElevationData("n3", 1));
        elevationData.add(new ElevationData("n4", 2));
        elevationData.add(new ElevationData("n5", 6));
        elevationData.add(new ElevationData("n6", 3));
        elevationData.add(new ElevationData("n7", 6));
        elevationData.add(new ElevationData("n8", 2));


        Mockito.doReturn(elevationData).when(mockDao).get(any(Set.class));
        return new ElenaGraph("simple_elevation.graphml", mockDao);
    }

    @Bean
    public AutoCompleter autoCompleter(){
        AbstractElenaGraph mockGraph = Mockito.mock(AbstractElenaGraph.class);
        List<String> locationNames = new ArrayList<>();
        locationNames.add("new york");
        locationNames.add("new jersey");
        locationNames.add("newark");
        locationNames.add("boston");
        locationNames.add("portland");
        Mockito.when(mockGraph.getLocationNames()).thenReturn(locationNames);
        return new TrieAutoCompleter(mockGraph);
    }

}

