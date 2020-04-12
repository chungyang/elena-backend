package com.elena.elena.autocomplete;

import com.elena.elena.model.AbstractElenaGraph;

import java.util.*;
import java.util.stream.Collectors;

public class TrieAutoCompleter implements AutoCompleter{

    private AbstractElenaGraph graph;
    Set<String> locationNames;
    Trie root;


    public TrieAutoCompleter(AbstractElenaGraph graph){
        this.graph = graph;
        locationNames = new HashSet<>();
        locationNames.add("new jersey");
        locationNames.add("boston");
        locationNames.add("newbie");
        locationNames.add("new york");

        root = new Trie(false, new ArrayList<>());
        populateTrie();
    }

    @Override
    public Collection<NameSuggestion> getNameSuggestions(String initialName) {

        Trie current = root;
        List<NameSuggestion> suggestions = new ArrayList<>();

        for(Character character : initialName.toLowerCase().toCharArray()){
            current = current.nextTries.get(character);
        }
        this.addWords(suggestions, current);

        return suggestions;
    }

    /**
     * Given a Trie and a collection, this method recursively adds
     * all words in the Trie
     */
    private void addWords(Collection<NameSuggestion> suggestions, Trie trie){

        if(trie.nextTries.isEmpty()){
            return;
        }

        for(Trie nextTrie : trie.nextTries.values()){
            if(nextTrie.isWord){
                String name = nextTrie.characters.stream().map(String::valueOf)
                        .collect(Collectors.joining());
                NameSuggestion nameSuggestion = new NameSuggestion(name);
                suggestions.add(nameSuggestion);
            }
            addWords(suggestions, nextTrie);
        }
    }

    private void populateTrie(){

//        Collection<AbstractElenaEdge> edges = this.graph.getAllEdges();
        Trie current = root;
//
//        for(AbstractElenaEdge edge : edges){
//            locationNames.add(edge.getProperties().get("name").toLowerCase());
//        }

        for(String locationName : locationNames){
            for(Character character : locationName.toCharArray()){
                current.insertIfabsent(character, false);
                current = current.nextTries.get(character);
            }
            current.isWord = true;
            current = root;
        }
    }

    private class Trie{

        private boolean isWord;
        private List<Character> characters;
        private Map<Character, Trie> nextTries;

        public Trie(Boolean isWord, List<Character> initialCharacters){
            this.isWord = isWord;
            this.nextTries = new HashMap<>();
            characters = new ArrayList<>();
            characters.addAll(initialCharacters);
        }

        /**
         * Returns true if it successfully inserts a key into trie,
         * returns false otherwise.
         */
        public boolean insertIfabsent(Character key, boolean isWord){
            if(!nextTries.containsKey(key)){
                nextTries.put(key, new Trie(isWord, this.characters));
                this.characters.add(key);
                return true;
            }
            return false;
        }
    }

    public static void main(String[] args){
        AutoCompleter autoCompleter = new TrieAutoCompleter(null);
        autoCompleter.getNameSuggestions("new");
    }
}
