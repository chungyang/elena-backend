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
    private final String SIMPLE_GRAPHML = "simple.graphml";

    @Bean("simple.graphml")
    public AbstractElenaGraph simpleGraph() throws IOException {

        Mockito.when(mockDao.get(any(Set.class))).thenReturn(new ArrayList<ElevationData>());
        return new ElenaGraph(SIMPLE_GRAPHML, mockDao);
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

