package com.elena.elena.routing;


public enum Algorithm {
    A_STAR, DIJKSTRA, YEN;

    private static final String A_STAR_NAME = "a*";
    private static final String YEN_NAME = "yen";

    public static Algorithm getAlgorithmByName(String name){

        switch (name.toLowerCase()){

            case A_STAR_NAME:
                return A_STAR;

            case YEN_NAME:
                return YEN;

            default:
                return DIJKSTRA;
        }
    }
}
