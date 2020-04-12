package com.elena.elena.routing;

public enum Algorithm {
    A_STAR, DIJKSTRA, A_STAR_YEN, DIJKSTRA_YEN;

    private static final String A_STAR_NAME = "a*";
    private static final String DIJKSTRA_NAME = "dijkstra";
    private static final String A_STAR_YEN_NAME = "a*_yen";

    public static Algorithm getAlgorithmByName(String name){

        switch (name.toLowerCase()){

            case A_STAR_NAME:
                return A_STAR;

            case DIJKSTRA_NAME:
                return DIJKSTRA;
            
            case A_STAR_YEN_NAME:
            	return A_STAR_YEN;
                
            default:
                return DIJKSTRA_YEN;
        }
    }
}
