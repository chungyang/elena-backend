package com.elena.elena.autocomplete;

import com.elena.elena.autocomplete.AutoCompleter;
import com.elena.elena.autocomplete.TrieAutoCompleter;
import com.elena.elena.model.AbstractElenaGraph;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

@ActiveProfiles("test")
@RunWith(SpringJUnit4ClassRunner.class)
public class AutoCompleteTest {

    @MockBean
    private AbstractElenaGraph mockGraph;
    private List<String> locationNames = new ArrayList<>();
    private AutoCompleter autoCompleter;

    @Before
    public void setup(){
        locationNames.add("new york");
        locationNames.add("new jersey");
        locationNames.add("newark");
        locationNames.add("boston");
        locationNames.add("portland");
        Mockito.when(mockGraph.getLocationNames()).thenReturn(locationNames);
        autoCompleter = new TrieAutoCompleter(mockGraph);
    }
    @Test
    public void testAutocomplete(){
        Assert.assertEquals(2, autoCompleter.getNameSuggestions("new ").size());
    }


}
