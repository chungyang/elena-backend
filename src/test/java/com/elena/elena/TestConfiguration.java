package com.elena.elena;

import com.elena.elena.autocomplete.AutoCompleter;
import com.elena.elena.autocomplete.TrieAutoCompleter;
import com.elena.elena.dao.ElevationDao;
import com.elena.elena.model.AbstractElenaGraph;
import com.elena.elena.model.AbstractElenaNode;
import com.elena.elena.model.ElenaGraph;
import com.elena.elena.util.Units;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

@Configuration
public class TestConfiguration {


    @MockBean
    private ElevationDao mockDao;
    private final String SIMPLE_GRAPHML = "simple.graphml";

    @Bean("simple.graphml")
    public AbstractElenaGraph simpleGraph() throws IOException {
        Mockito.when(mockDao.get(any(Collection.class), eq(Units.METRIC))).thenReturn(6);
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

