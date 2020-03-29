package com.elena.elena.routing;


public enum ElevationMode {
    MIN, MAX;

    private static final String MIN_MODE = "min";

    public static ElevationMode getElevationMode(String name){

        switch (name.toLowerCase()){

            case MIN_MODE:
                return MIN;

            default:
                return MAX;
        }
    }
}
