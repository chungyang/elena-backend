package com.elena.elena.routing;


public enum ElevationMode {
    MIN, MAX;

    public static ElevationMode getElevationMode(String name){

        switch (name.toLowerCase()){

            case "min":
                return MIN;

            default:
                return MAX;
        }
    }
}
