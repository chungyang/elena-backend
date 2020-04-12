package com.elena.elena.routing;

public enum Weight {
	DISTANCE, ELEVATION;
	
	private static final String DISTANCE_NAME = "distance";
	
	public static Weight getWeightByName(String name){

        switch (name.toLowerCase()){

            case DISTANCE_NAME:
                return DISTANCE;

            default:
                return ELEVATION;
        }
    }
}