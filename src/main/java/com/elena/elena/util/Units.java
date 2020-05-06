package com.elena.elena.util;

public enum Units {
    US("Feet"), METRIC("Meters");

    private String unit;

    Units(String unit){
        this.unit = unit;
    }

    public String getUnit(){
        return unit;
    }
}
