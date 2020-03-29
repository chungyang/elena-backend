package com.elena.elena.routing;


public enum Algorithm {
    A_STAR, DIJKSTRA;

    private static final String A_STAR_NAME = "a*";

    public static Algorithm getMatchingAlogrithm(String name){

        switch (name.toLowerCase()){

            case A_STAR_NAME:
                return A_STAR;

            default:
                return DIJKSTRA;
        }
    }
}
