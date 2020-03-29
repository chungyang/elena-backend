package com.elena.elena.routing;


public enum Algorithm {
    A_STAR, DIJKSTRA;

    public static Algorithm getMatchingAlogrithm(String name){

        switch (name.toLowerCase()){

            case "a*":
                return A_STAR;

            default:
                return DIJKSTRA;
        }
    }
}
