package com.elena.elena.autocomplete;

import com.elena.elena.model.AbstractElenaGraph;

import java.util.Collection;

public interface AutoCompleter {

    Collection<String> getNameSuggestions(String initialName);

}
